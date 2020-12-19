export default {
    name: 'Documentation',
    mounted() {
        hljs.initHighlightingOnLoad()
    },
    template: `
        <div>
            <h1 id="collection-documentation">Collection Documentation</h1>
            <p>Collection is a server-less embedded database ideal for small web applications. It&#39;s based on the open source project <a href="https://www.dizitart.org/nitrite-database.html">Nitrite Database</a>.</p>
            <p><strong>It features:</strong></p>
            <ul>
            <li>Embedded key-value object store</li>
            <li>Single file store</li>
            <li>Very fast and lightweight MongoDB like API</li>
            <li>Indexing</li>
            <li>Full text search capability</li>
            <li>Observable store</li>
            </ul>
            <p><em>Requires Java Express version 0.5.0 and above!</em></p>
            <h2 id="table-of-content">Table of content</h2>
            <ul>
                <li><a href="#getting-started">Getting started</a></li>
                <li><a href="#collectionoptions">CollectionOptions</a></li>
                <ul>
                    <li><a href="#important-note">Important note!</a></li>
                </ul>
                <li><a href="#annotations">Annotations</a></li>
                <li><a href="#collection-methods">Collection methods</a></li>
                <ul>
                    <li><a href="#filters">Filters</a></li>
                    <li><a href="#findoptions">FindOptions</a></li>
                </ul>
                <li><a href="#examples">Examples</a></li>
            </ul>
            <h2 id="getting-started">Getting started</h2>
            <p>The Express app has an embedded nosql database, ready to be used if you enable it by adding <code>app.enableCollections()</code> right after app is instantiated. 
            This will create a database-file in your project, and is easy to deploy or share.
            <br>
            When collections are enabled you can use the static <code>collection()</code>-method to manipulate the database. 
            <strong>collection()</strong> takes either a String with the classname, case sensitive, or the Class itself. </p>
            <pre><code class="lang-java">
import static express.database.Database.collection;

Express <span class="hljs-keyword">app</span> = new Express();
<span class="hljs-comment">// creates a database-file in /db-folder called 'embedded.db'</span>
<span class="hljs-keyword">app</span>.enableCollections(); 
<span class="hljs-comment">// creates the file at target path</span>
<span class="hljs-keyword">app</span>.enableCollections(String dbPath); 


User john = new User(<span class="hljs-string">"John"</span>).
<span class="hljs-comment">// generates an UUID</span>
collection(<span class="hljs-string">"User"</span>).<span class="hljs-keyword">save</span>(john); 

User jane = collection(<span class="hljs-string">"User"</span>).findById(<span class="hljs-string">"xxxxxxxx-xxxx-4xxx-8xxx-xxxxxxxxxxxx"</span>);

jane.setAge(30);
<span class="hljs-comment">// updates model with same UUID</span>
collection(<span class="hljs-string">"User"</span>).<span class="hljs-keyword">save</span>(jane); 

<span class="hljs-comment">// delete Jane</span>
collection(<span class="hljs-string">"User"</span>).deleteById(<span class="hljs-string">"xxxxxxxx-xxxx-4xxx-8xxx-xxxxxxxxxxxx"</span>); 

<span class="hljs-keyword">List</span>&lt;User&gt; users = collection(<span class="hljs-string">"User"</span>).find();
<span class="hljs-keyword">List</span>&lt;User&gt; users = collection(User.<span class="hljs-keyword">class</span>).find();

<span class="hljs-keyword">List</span>&lt;User&gt; usersNamedJohn = collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-keyword">eq</span>(<span class="hljs-string">"name"</span>, <span class="hljs-string">"John"</span>));
            </code></pre>
            <p>Watch a collection on changes</p>
            <pre><code class="lang-java">
<span class="hljs-comment">// watchData has 2 fields. </span>
<span class="hljs-comment">// getEvent() is the event triggered - 'insert', 'update' or 'delete'</span>
<span class="hljs-comment">// getData() is a list with effected models</span>
collection(<span class="hljs-string">"User"</span>).watch(watchData -&gt; {
    <span class="hljs-built_in">List</span>&lt;User&gt; effectedUsers = watchData.getData();

    <span class="hljs-keyword">switch</span>(watchData.getEvent()) {
        <span class="hljs-keyword">case</span> <span class="hljs-string">"insert"</span>: <span class="hljs-comment">// on created model</span>
        <span class="hljs-keyword">break</span>;

        <span class="hljs-keyword">case</span> <span class="hljs-string">"update"</span>: <span class="hljs-comment">// on updated model</span>
        <span class="hljs-keyword">break</span>;

        <span class="hljs-keyword">case</span> <span class="hljs-string">"delete"</span>: <span class="hljs-comment">// on deleted model</span>
        <span class="hljs-keyword">break</span>;
    }
});
            </code></pre>
            <h3 id="collectionoptions">CollectionOptions</h3>
<p>CollectionOptions can be passed when enabling collections to set certain options.
Options available are:</p>
<ul>
<li><em>CollectionOptions.ENABLE_WATCHER</em> - Enables Server Side Events listener on collection changes</li>
<li><em>CollectionOptions.DISABLE_BROWSER</em> - Disables collection browser (good when deploying)</li>
</ul>
<p>You can pass one or multiple options when enabling collections:</p>
<pre><code class="lang-java"><span class="hljs-type">Express</span> app = <span class="hljs-function"><span class="hljs-keyword">new</span> <span class="hljs-title">Express</span>();
<span class="hljs-title">app</span>.<span class="hljs-title">enableCollections</span>(<span class="hljs-type">CollectionOptions</span>.<span class="hljs-type">ENABLE_WATCHER</span>, <span class="hljs-type">CollectionOptions</span>.<span class="hljs-type">DISABLE_BROWSER</span>);</span>
</code></pre>
<p><strong>ENABLE_WATCHER</strong></p>
<p>This starts an event stream endpoint in the database that will send a Server Side Event when a change happens.</p>
<p>To listen to these events on the client you have to create a connection to <code>&#39;/watch-collections&#39;</code> with an <code>EventSource</code>.</p>
<pre><code class="lang-js"><span class="hljs-keyword">let</span> colls = <span class="hljs-keyword">new</span> EventSource(<span class="hljs-string">'/watch-collections'</span>)
</code></pre>
<p>With the eventSource you can add listeners to each model in the collection.</p>
<pre><code class="lang-js"><span class="hljs-regexp">//</span> listen to changes to the <span class="hljs-string">'BlogPost'</span> collection 
colls.addEventListener(<span class="hljs-string">'BlogPost'</span>, <span class="hljs-function"><span class="hljs-params">(messageEvent)</span> =&gt;</span> {
    <span class="hljs-regexp">//</span> handle event
}

<span class="hljs-regexp">//</span> listen to changes to the <span class="hljs-string">'Message'</span> collection 
colls.addEventListener(<span class="hljs-string">'Message'</span>, <span class="hljs-function"><span class="hljs-params">(messageEvent)</span> =&gt;</span> {
    <span class="hljs-regexp">//</span> handle event
});
</code></pre>
<h4>Examples</h4>
<p>Java:</p>
<pre><code class="lang-java"><span class="hljs-type">Express</span> app = <span class="hljs-function"><span class="hljs-keyword">new</span> <span class="hljs-title">Express</span>();
<span class="hljs-title">app</span>.<span class="hljs-title">enableCollections</span>(<span class="hljs-type">CollectionOptions</span>.<span class="hljs-type">ENABLE_WATCHER</span>);</span>
</code></pre>
<p>JavaScript:</p>
<pre><code class="lang-js"><span class="hljs-keyword">let</span> colls = <span class="hljs-keyword">new</span> EventSource(<span class="hljs-string">'/watch-collections'</span>);

colls.addEventListener(<span class="hljs-string">'BlogPost'</span>, (messageEvent) =&gt; {
    <span class="hljs-keyword">const</span> { event, data } = <span class="hljs-built_in">JSON</span>.parse(messageEvent.data);
    <span class="hljs-built_in">console</span>.log(<span class="hljs-string">"BlogPost event:"</span>, event, data);

    <span class="hljs-keyword">switch</span>(event) {
        <span class="hljs-keyword">case</span> <span class="hljs-string">'insert'</span>:
            <span class="hljs-comment">// add new post to list</span>
            posts.push(data[<span class="hljs-number">0</span>]);
        <span class="hljs-keyword">break</span>;
        <span class="hljs-keyword">case</span> <span class="hljs-string">'update'</span>:
            <span class="hljs-comment">// do something on update</span>
        <span class="hljs-keyword">break</span>;
        <span class="hljs-keyword">case</span> <span class="hljs-string">'delete'</span>:
            <span class="hljs-comment">// remove deleted post from list</span>
            posts = posts.filter(<span class="hljs-function"><span class="hljs-params">post</span> =&gt;</span> post.id !== data[<span class="hljs-number">0</span>].id);
        <span class="hljs-keyword">break</span>;
    }

    <span class="hljs-comment">// update </span>
    renderPosts();
});

colls.addEventListener(<span class="hljs-string">'Message'</span>, (messageEvent) =&gt; {
    <span class="hljs-keyword">const</span> { event, data } = <span class="hljs-built_in">JSON</span>.parse(messageEvent.data);
    <span class="hljs-built_in">console</span>.log(<span class="hljs-string">'Message event:'</span>, event, data);
});
</code></pre>
<p><strong>DISABLE_BROWSER</strong></p>
<p>This will simple disable the collection browser. This might be a good idea to save CPU and RAM when deploying. </p>
<pre><code class="lang-java"><span class="hljs-type">Express</span> app = <span class="hljs-function"><span class="hljs-keyword">new</span> <span class="hljs-title">Express</span>();
<span class="hljs-title">app</span>.<span class="hljs-title">enableCollections</span>(<span class="hljs-type">CollectionOptions</span>.<span class="hljs-type">DISABLE_BROWSER</span>);</span>
</code></pre>

            <div class="important-div">
                <h3 id="important-note">Important note!</h3>
                <p>After a model is saved to the collection, the class with <strong>@Model</strong> annotation <strong>CANNOT</strong> be moved to another package or renamed. This will corrupt the database-file, and will have to be removed. 
                Keep backups!</p>
                <p>Changing the name of a field will not corrupt the database, but will remove the value from all models.</p>
            </div>
            <h2 id="annotations">Annotations</h2>
            <p>For the collections to work the following two annotations must be present in at least one class.</p>
            <h3 id="-model-annotation">@Model Annotation</h3>
            <p>Marks a class to be used with a collection. Is required if an object is going to be saved to the collection.</p>
            <h3 id="-id-annotation">@Id Annotation</h3>
            <p>Each object in a Collection must be uniquely identified by a field marked with <strong>@Id</strong> annotation. The collection maintains an unique index on that field to identify the objects.
            If no id is manually set, the Collection will generate an UUID to that field when inserted or saved. </p>
            <pre><code class="lang-java">
<span class="hljs-selector-tag">import</span> <span class="hljs-selector-tag">express</span><span class="hljs-selector-class">.database</span><span class="hljs-selector-class">.Model</span>;
<span class="hljs-selector-tag">import</span> <span class="hljs-selector-tag">org</span><span class="hljs-selector-class">.dizitart</span><span class="hljs-selector-class">.no2</span><span class="hljs-selector-class">.objects</span><span class="hljs-selector-class">.Id</span>;

@<span class="hljs-keyword">Model</span>
public class MyType {

    @<span class="hljs-keyword">Id</span>
    private String id;
    <span class="hljs-selector-tag">private</span> <span class="hljs-selector-tag">String</span> <span class="hljs-selector-tag">name</span>;
}
            </code></pre>
            <p>The collection provides a set of annotations for model objects while using it in a collection. The annotations are to let the collection know about various information about the <strong>model</strong> while constructing it. It also helps to reduce some boilerplate code.</p>
            <p><strong>@Index</strong> is required to do <strong>text()</strong> or <strong>regex()</strong> filtering on a field. It can only be used within a <strong>@Indices</strong> annotation.
            <strong>Index types are:</strong></p>
            <ul>
            <li>IndexType.Unique - used with unique fields</li>
            <li>IndexType.NonUnique - used with single value duplicate fields</li>
            <li>IndexType.FullText - used with multiple word fields, NonUnique</li>
            </ul>
            <p>Example</p>
            <pre><code class="lang-java">
<span class="hljs-comment">// Employee class</span>
<span class="hljs-variable">@Indices</span>({
        <span class="hljs-variable">@Index</span>(value = <span class="hljs-string">"joinDate"</span>, type = IndexType.NonUnique),
        <span class="hljs-variable">@Index</span>(value = <span class="hljs-string">"name"</span>, type = IndexType.Unique)
})
<span class="hljs-variable">@Model</span>
public class Employee {
    <span class="hljs-variable">@Id</span>
    private String id;
    <span class="hljs-selector-tag">private</span> <span class="hljs-selector-tag">Date</span> <span class="hljs-selector-tag">joinDate</span>;
    <span class="hljs-selector-tag">private</span> <span class="hljs-selector-tag">String</span> <span class="hljs-selector-tag">name</span>;
    <span class="hljs-selector-tag">private</span> <span class="hljs-selector-tag">String</span> <span class="hljs-selector-tag">address</span>;

    <span class="hljs-comment">// ... public getters and setters</span>
}
            </code></pre>
            <h2 id="collection-methods">Collection methods</h2>
            <p>To use the collection you need to add which model to query for in the collection parameter, ex <code>collection(&quot;User&quot;)</code> will only query for Users. </p>
            <p><strong>Table 1. Collection methods</strong></p>
            <table>
            <thead>
            <tr>
            <th>Operation</th>
            <th>Method</th>
            <th>Description</th>
            </tr>
            </thead>
            <tbody>
            <tr>
            <td>Get all models</td>
            <td>find(Filter, SortOptions)</td>
            <td>Returns a list with objects. If no filter is used find() will return ALL models.</td>
            </tr>
            <tr>
            <td>Get one model</td>
            <td>findOne(Filter)</td>
            <td>Returns first found model.</td>
            </tr>
            <tr>
            <td>Get model with id</td>
            <td>findById(String)</td>
            <td>Returns the object with mathing id.</td>
            </tr>
            <tr>
            <td>Create new model</td>
            <td>insert(Object)</td>
            <td>Creates a new model in the collection. Generates an UUID if no id is present. Can insert an array of models.</td>
            </tr>
            <tr>
            <td>Create or Update a model</td>
            <td>save(Object)</td>
            <td>Creates a new model in the collection if no id is present. If theres an id save() will update the existing model in the collection. Can save an array of models.</td>
            </tr>
            <tr>
            <td>Update models</td>
            <td>update(Filter, Object)</td>
            <td>Update all models matching the filter.</td>
            </tr>
            <tr>
            <td>Update a model with id</td>
            <td>updateById(String)</td>
            <td>Updates the model with matching id.</td>
            </tr>
            <tr>
            <td>Delete models</td>
            <td>delete(Filter)</td>
            <td>Deletes all models matching the filter.</td>
            </tr>
            <tr>
            <td>Delete a model with id</td>
            <td>deleteById(String)</td>
            <td>Deletes the model with matching id.</td>
            </tr>
            <tr>
            <td>Watch a collection</td>
            <td>watch(lambda)</td>
            <td>Register a watcher that triggers on changes in the collection.</td>
            </tr>
            </tbody>
            </table>
            <h3 id="filters">Filters</h3>
            <p>Filters are the selectors in the collection’s find operation. It matches models in the collection depending on the criteria provided and returns a list of objects.</p>
            <p>Make sure you import the static method <strong>ObjectFilters</strong>.</p>
<pre><code class="lang-java">import static org.dizitart.no2.objects.filters.ObjectFilters.*;</code></pre>
            <p><strong>Table 2. Comparison Filter</strong></p>
            <table>
            <thead>
            <tr>
            <th>Filter</th>
            <th>Method</th>
            <th>Description</th>
            </tr>
            </thead>
            <tbody>
            <tr>
            <td>Equals</td>
            <td>eq(String, Object)</td>
            <td>Matches values that are equal to a specified value.</td>
            </tr>
            <tr>
            <td>Greater</td>
            <td>gt(String, Object)</td>
            <td>Matches values that are greater than a specified value.</td>
            </tr>
            <tr>
            <td>GreaterEquals</td>
            <td>gte(String, Object)</td>
            <td>Matches values that are greater than or equal to a specified value.</td>
            </tr>
            <tr>
            <td>Lesser</td>
            <td>lt(String, Object)</td>
            <td>Matches values that are less than a specified value.</td>
            </tr>
            <tr>
            <td>LesserEquals</td>
            <td>lte(String, Object)</td>
            <td>Matches values that are less than or equal to a specified value.</td>
            </tr>
            <tr>
            <td>In</td>
            <td>in(String, Object[])</td>
            <td>Matches any of the values specified in an array.</td>
            </tr>
            <tr>
            <td>NotIn</td>
            <td>notIn(String, Object[])</td>
            <td>Matches none of the values specified in an array.</td>
            </tr>
            </tbody>
            </table>
            <p><strong>Table 3. Logical Filters</strong></p>
            <table>
            <thead>
            <tr>
            <th>Filter</th>
            <th>Method</th>
            <th>Description</th>
            </tr>
            </thead>
            <tbody>
            <tr>
            <td>Not</td>
            <td>not(Filter)</td>
            <td>Inverts the effect of a filter and returns results that do not match the filter.</td>
            </tr>
            <tr>
            <td>Or</td>
            <td>or(Filter[])</td>
            <td>Joins filters with a logical OR returns all ids of the models that match the conditions of either filter.</td>
            </tr>
            <tr>
            <td>And</td>
            <td>and(Filter[])</td>
            <td>Joins filters with a logical AND returns all ids of the models that match the conditions of both filters.</td>
            </tr>
            </tbody>
            </table>
            <p>Table 4. Array Filter</p>
            <table>
            <thead>
            <tr>
            <th>Filter</th>
            <th>Method</th>
            <th>Description</th>
            </tr>
            </thead>
            <tbody>
            <tr>
            <td>Element Match</td>
            <td>elemMatch(String, Filter)</td>
            <td>Matches models that contain an array field with at least one element that matches the specified filter.</td>
            </tr>
            </tbody>
            </table>
            <p><strong>Table 5. Text Filters</strong>
            <em>Note</em>: For these filters to work the field must be indexed. See <a href="#annotations">Annotations</a></p>
            <table>
            <thead>
            <tr>
            <th>Filter</th>
            <th>Method</th>
            <th>Description</th>
            </tr>
            </thead>
            <tbody>
            <tr>
            <td>Text</td>
            <td>text(String, String)</td>
            <td>Performs full-text search.</td>
            </tr>
            <tr>
            <td>Regex</td>
            <td>regex(String, String)</td>
            <td>Selects models where values match a specified regular expression.</td>
            </tr>
            </tbody>
            </table>
            <h3 id="findoptions">FindOptions</h3>
            <p>A FindOptions is used to specify search options. It provides pagination as well as sorting mechanism.</p>

<pre><code class="lang-java">import static org.dizitart.no2.FindOptions.*;</code></pre>

            <p>Example</pre>
<pre><code class="lang-java">// sorts all models <span class="hljs-built_in">by</span> age <span class="hljs-built_in">in</span> ascending order <span class="hljs-keyword">then</span> take <span class="hljs-built_in">first</span> <span class="hljs-number">10</span> models and <span class="hljs-keyword">return</span> <span class="hljs-built_in">as</span> a List
List&lt;User&gt; users = collection(<span class="hljs-string">"User"</span>).find(sort(<span class="hljs-string">"age"</span>, SortOrder.Ascending).thenLimit(<span class="hljs-number">0</span>, <span class="hljs-number">10</span>));
</code></pre>
<pre><code class="lang-java"><span class="hljs-comment">// sorts the models by age in ascending order</span>
<span class="hljs-built_in">List</span>&lt;User&gt; users = collection(<span class="hljs-string">"User"</span>).<span class="hljs-built_in">find</span>(<span class="hljs-built_in">sort</span>(<span class="hljs-string">"age"</span>, SortOrder.Ascending));
</code></pre>
<pre><code class="lang-java"><span class="hljs-comment">// sorts the models by name in ascending order with custom collator</span>
<span class="hljs-built_in">List</span>&lt;User&gt; users = collection(<span class="hljs-string">"User"</span>).find(sort(<span class="hljs-string">"name"</span>, SortOrder.<span class="hljs-keyword">Ascending</span>, Collator.getInstance(<span class="hljs-built_in">Locale</span>.FRANCE)));
</code></pre>
<pre><code class="lang-java"><span class="hljs-comment">// fetch 10 models starting from offset = 2</span>
List&lt;User&gt; users = collection(<span class="hljs-string">"User"</span>).find(limit(<span class="hljs-number">2</span>, <span class="hljs-number">10</span>));
</code></pre>

<h2 id="examples">Examples</h2>
<p><strong>and()</strong></p>
<pre><code class="lang-java">// matches all models where <span class="hljs-string">'age'</span> field has value as <span class="hljs-number">30</span> <span class="hljs-keyword">and</span>
// <span class="hljs-string">'name'</span> field has value as John Doe
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-keyword">and</span>(e<span class="hljs-string">q("age", 30)</span>, e<span class="hljs-string">q("name", "John Doe")</span>));
</code></pre>
<p><strong>or()</strong></p>
<pre><code class="lang-java">// matches all models where <span class="hljs-string">'age'</span> field has value as <span class="hljs-number">30</span> <span class="hljs-keyword">or</span>
// <span class="hljs-string">'name'</span> field has value as John Doe
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-keyword">or</span>(e<span class="hljs-string">q("age", 30)</span>, e<span class="hljs-string">q("name", "John Doe")</span>));
</code></pre>
<p><strong>not()</strong></p>
<pre><code class="lang-java">// matches all models where <span class="hljs-string">'age'</span> field has value <span class="hljs-keyword">not</span> equals to <span class="hljs-number">30</span>
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-keyword">not</span>(e<span class="hljs-string">q("age", 30)</span>));
</code></pre>
<p><strong>eq()</strong></p>
<pre><code class="lang-java">// matches all models where <span class="hljs-string">'age'</span> field has value as <span class="hljs-number">30</span>
collection(<span class="hljs-string">"User"</span>).find(e<span class="hljs-string">q("age", 30)</span>);
</code></pre>
<p><strong>gt()</strong></p>
<pre><code class="lang-java"><span class="hljs-comment">// matches all models where 'age' field has value greater than 30</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(gt(<span class="hljs-string">"age"</span>, <span class="hljs-number">30</span>));
</code></pre>
<p><strong>gte()</strong></p>
<pre><code class="lang-java">// <span class="hljs-keyword">matches</span> all models where 'age' field has value <span class="hljs-keyword">greater</span> than <span class="hljs-keyword">or</span> <span class="hljs-keyword">equal</span> to <span class="hljs-number">30</span>
collection(<span class="hljs-string">"User"</span>).find(gte(<span class="hljs-string">"age"</span>, <span class="hljs-number">30</span>));
</code></pre>
<p><strong>lt()</strong></p>
<pre><code class="lang-java"><span class="hljs-comment">// matches all models where 'age' field has value less than 30</span>
<span class="hljs-selector-tag">collection</span>(<span class="hljs-string">"User"</span>)<span class="hljs-selector-class">.find</span>(lt(<span class="hljs-string">"age"</span>, <span class="hljs-number">30</span>));
</code></pre>
<p><strong>lte()</strong></p>
<pre><code class="lang-java">// matches <span class="hljs-keyword">all</span> models where <span class="hljs-symbol">'age</span>' field has value lesser than <span class="hljs-keyword">or</span> equal <span class="hljs-keyword">to</span> <span class="hljs-number">30</span>
collection(<span class="hljs-string">"User"</span>).find(lte(<span class="hljs-string">"age"</span>, <span class="hljs-number">30</span>));
</code></pre>
<p><strong>in()</strong></p>
<pre><code class="lang-java">// matches all models <span class="hljs-keyword">where</span> <span class="hljs-string">'age'</span> field has <span class="hljs-keyword">value</span> <span class="hljs-keyword">in</span> [<span class="hljs-number">20</span>, <span class="hljs-number">30</span>, <span class="hljs-number">40</span>]
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-keyword">in</span>(<span class="hljs-string">"age"</span>, <span class="hljs-number">20</span>, <span class="hljs-number">30</span>, <span class="hljs-number">40</span>));
</code></pre>
<p><strong>notIn()</strong></p>
<pre><code class="lang-java">// matches <span class="hljs-keyword">all</span> models where <span class="hljs-symbol">'age</span>' field does <span class="hljs-keyword">not</span> have value <span class="hljs-keyword">in</span> [<span class="hljs-number">20</span>, <span class="hljs-number">30</span>, <span class="hljs-number">40</span>]
collection(<span class="hljs-string">"User"</span>).find(notIn(<span class="hljs-string">"age"</span>, <span class="hljs-number">20</span>, <span class="hljs-number">30</span>, <span class="hljs-number">40</span>));
</code></pre>
<p><strong>elemMatch()</strong></p>
<pre><code class="lang-java">// matches all models which has an<span class="hljs-built_in"> array </span>field - 'color'<span class="hljs-built_in"> and </span>the<span class="hljs-built_in"> array
</span>// contains a value - 'red'.
collection(<span class="hljs-string">"User"</span>).find(elemMatch(<span class="hljs-string">"color"</span>, eq(<span class="hljs-string">"$"</span>, <span class="hljs-string">"red"</span>));
</code></pre>
<p><strong>text()</strong></p>
<pre><code class="lang-java">// matches <span class="hljs-keyword">all</span> models where <span class="hljs-symbol">'address</span>' field has a word <span class="hljs-symbol">'roads</span>'.
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-literal">text</span>(<span class="hljs-string">"address"</span>, <span class="hljs-string">"roads"</span>));

// matches <span class="hljs-keyword">all</span> models where <span class="hljs-symbol">'address</span>' field has word that starts <span class="hljs-keyword">with</span> '<span class="hljs-number">11</span>A'.
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-literal">text</span>(<span class="hljs-string">"address"</span>, <span class="hljs-string">"11a*"</span>));

// matches <span class="hljs-keyword">all</span> models where <span class="hljs-symbol">'address</span>' field has a word that ends <span class="hljs-keyword">with</span> <span class="hljs-symbol">'Road</span>'.
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-literal">text</span>(<span class="hljs-string">"address"</span>, <span class="hljs-string">"*road"</span>));

// matches <span class="hljs-keyword">all</span> models where <span class="hljs-symbol">'address</span>' field has a word that contains a <span class="hljs-literal">text</span> <span class="hljs-symbol">'oa</span>'.
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-literal">text</span>(<span class="hljs-string">"address"</span>, <span class="hljs-string">"*oa*"</span>));

// matches <span class="hljs-keyword">all</span> models where <span class="hljs-symbol">'address</span>' field has words like '<span class="hljs-number">11</span>a' <span class="hljs-keyword">and</span> <span class="hljs-symbol">'road</span>'.
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-literal">text</span>(<span class="hljs-string">"address"</span>, <span class="hljs-string">"11a road"</span>));

// matches <span class="hljs-keyword">all</span> models where <span class="hljs-symbol">'address</span>' field has word <span class="hljs-symbol">'road</span>' <span class="hljs-keyword">and</span> another word that start <span class="hljs-keyword">with</span> '<span class="hljs-number">11</span>a'.
collection(<span class="hljs-string">"User"</span>).find(<span class="hljs-literal">text</span>(<span class="hljs-string">"address"</span>, <span class="hljs-string">"11a* road"</span>));
</code></pre>
<p><strong>regex()</strong></p>
<pre><code class="lang-java">// matches <span class="hljs-keyword">all</span> models where <span class="hljs-symbol">'name</span>' value starts <span class="hljs-keyword">with</span> <span class="hljs-symbol">'jim</span>' <span class="hljs-keyword">or</span> <span class="hljs-symbol">'joe</span>'.
collection(<span class="hljs-string">"User"</span>).find(regex(<span class="hljs-string">"name"</span>, <span class="hljs-string">"^(jim|joe).*"</span>));
</code></pre>

        </div >
    `
}