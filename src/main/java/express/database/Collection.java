package express.database;

import org.dizitart.no2.FindOptions;
import org.dizitart.no2.RemoveOptions;
import org.dizitart.no2.event.ChangedItem;
import org.dizitart.no2.mapper.JacksonMapper;
import org.dizitart.no2.mapper.NitriteMapper;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.ObjectRepository;

import java.lang.reflect.Field;
import java.util.*;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

/**
 * @author Johan Wirén
 *
 * This class will serve as an adapter for easy querying the
 * embedded database.
 */
@SuppressWarnings("unchecked")
public class Collection {
    private final ObjectRepository repo;
    private final Class klass;
    private String idField;

    public Collection(ObjectRepository repo, Class klass, String idField) {
        this.repo = repo;
        this.klass = klass;
        // set id field name to optimise reflections
        this.idField = idField;
    }

    public List find() {
        return repo.find().toList();
    }

    public List find(ObjectFilter filter) {
        return repo.find(filter).toList();
    }

    public List find(FindOptions options) {
        return repo.find(options).toList();
    }

    public List find(ObjectFilter filter, FindOptions options) {
        return repo.find(filter, options).toList();
    }

    public <T> T findOne(ObjectFilter filter) {
        List list = find(filter);
        return list.isEmpty() ? null : (T) list.get(0);
    }

    public <T> T findOne(ObjectFilter filter, FindOptions options) {
        List list = find(filter, options);
        return list.isEmpty() ? null : (T) list.get(0);
    }

    public <T> T findById(String id) {
        Map<String, String> field = getIdField();
        List list = find(eq(field.get("name"), id));
        return list.isEmpty() ? null : (T) list.get(0);
    }

    public <T> T insert(Object model) {
        getIdField(model);
        repo.insert(model);
        return (T) model;
    }

    public List insert(List models) {
        return Arrays.asList(insert(models.toArray()));
    }

    public <T> T[] insert(Object[] models) {
        for(Object m : models) getIdField(m);
        repo.insert(models);
        return (T[]) models;
    }

    public List save(List models) {
        return Arrays.asList(save(models.toArray()));
    }

    public <T> T[] save(Object[] models) {
        for(Object model : models) save(model);
        return (T[]) models;
    }

    public <T> T save(Object model) {
        Map<String, String> idValues = getIdField(model);
        repo.update(eq(idValues.get("name"), idValues.get("id")), model, true);
        return (T) model;
    }

    public int update(Object model) {
        return repo.update(model).getAffectedCount();
    }

    public int update(ObjectFilter filter, Object model) {
        getIdField(model);
        return repo.update(filter, model).getAffectedCount();
    }

    public int updateById(String id, Object model) {
        Map<String, String> field = getIdField();
        return repo.update(eq(field.get("name"), id), model).getAffectedCount();
    }

    public int delete(Object model) {
        return repo.remove(model).getAffectedCount();
    }

    public int delete(ObjectFilter filter) {
        return repo.remove(filter).getAffectedCount();
    }

    public int delete(ObjectFilter filter, RemoveOptions options) {
        return repo.remove(filter, options).getAffectedCount();
    }

    public int deleteById(String id) {
        Map<String, String> field = getIdField();
        return repo.remove(eq(field.get("name"), id)).getAffectedCount();
    }

    public int deleteById(String id, RemoveOptions options) {
        Map<String, String> field = getIdField();
        return repo.remove(eq(field.get("name"), id), options).getAffectedCount();
    }

    public void watch(WatchHandler watcher) {
        NitriteMapper mapper = new JacksonMapper();
        repo.register(changeInfo -> {
            WatchData watchData = new WatchData();
            watchData.setEvent(changeInfo.getChangeType().toString().equals("REMOVE") ? "delete" : changeInfo.getChangeType().toString().toLowerCase());
            List items = new ArrayList();

            for (ChangedItem item : changeInfo.getChangedItems()) {
                items.add(mapper.asObject(item.getDocument(), klass));
            }
            watchData.setData(items);
            watcher.handler(watchData);
        });
    }

    private Map<String, String> getIdField() {
        try {
            return getIdField(klass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, String> getIdField(Object model) {
        Map<String, String> idValues = new HashMap<>();

        if(idField != null) {
            try {
                Field field = model.getClass().getDeclaredField(idField);
                field.setAccessible(true);
                if(field.get(model) == null) {
                    field.set(model, UUID.randomUUID().toString());
                }
                idValues.putIfAbsent("name", field.getName());
                idValues.putIfAbsent("id", (String) field.get(model));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                for(Field field : model.getClass().getDeclaredFields()) {
                    if(field.isAnnotationPresent(Id.class)) {
                        field.setAccessible(true);
                        if(field.get(model) == null) {
                            field.set(model, UUID.randomUUID().toString());
                        }
                        idValues.putIfAbsent("name", field.getName());
                        idValues.putIfAbsent("id", (String) field.get(model));
                        break;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return idValues;
    }
}
