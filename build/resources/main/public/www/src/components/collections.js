const { watch, computed, onMounted, onBeforeUnmount, ref } = Vue
const { useStore } = Vuex

export default {
    name: 'Collections',
    template: `
        <div>
            <div class="coll-header">
                <h2>{{ activeColl }}</h2>
                <button @click="openDropCollModal" class="button-warn">Drop</button>
                <button @click="exportJSON">Export</button>
                <button v-if="isImporting"><div class="loader"></div></button>
                <button v-else :disabled="jsonFilesNotLoaded" @click="importJSON">Import</button>
                <input type="file" accept="application/json" ref="jsonFile" @change="filesLoaded">
            </div>
                <p v-if="errorMessage" id="important-note">
                    Failed to import json: <br>
                    {{ errorMessage }}
                </p>
            <div v-if="fetchingColls" class="loader">Loading...</div>
            <div v-show="!fetchingColls" @click="jsonClick" ref="jsonDiv" id="json"></div>
        </div>
    `,
    setup() {
        const store = useStore()
        const jsonDiv = ref()
        const jsonFile = ref()
        const errorMessage = ref('')
        const isImporting = ref(false)
        const jsonFilesNotLoaded = ref(true)
        const activeColl = computed(() => store.state.activeColl)
        const collection = computed(() => store.state.collections)
        const fetchingColls = computed(() => store.state.fetchingColls)
        const jsonViewer = new JSONViewer()

        // render json tree based on active collection
        const renderJson = () => {
            jsonViewer.showJSON(store.getters.activeCollection, -1, 2)
            setTimeout(() => {
                document.querySelectorAll('ul[data-level="1"]').forEach(ul => ul.insertAdjacentHTML('afterBegin', '<span class="delete-x">X</span>'))
            }, 0);
        }
        
        // re-render on collection change
        watch([activeColl, collection], () => {
            renderJson(store.getters.activeCollection)
        })

        const filesLoaded = () => {
            if(jsonFile.value && jsonFile.value.files.length < 1) {
                jsonFilesNotLoaded.value = true
            } else {
                jsonFilesNotLoaded.value = false
            }
        }
        
        // init json tree render
        onMounted(() => {
            setTimeout(() => {
                jsonDiv.value.appendChild(jsonViewer.getContainer())
                renderJson(store.getters.activeCollection)
            }, 10);
        })

        onBeforeUnmount(() => {
            // clear json tree on unmount to prevent duplicate trees
            jsonDiv.value.innerHTML = ''
        })

        const jsonClick = e => {
            // get object with id
            let topLevelObject = e.target.querySelector('ul[data-level="1"]')               // check down
            !topLevelObject && (topLevelObject = e.target.closest('ul[data-level="1"]'))    // else check up

            const idField = store.getters.activeField || 'id'
            const id = topLevelObject && [...topLevelObject.querySelectorAll('li')]
                .find(n => n.textContent.startsWith(idField + ': '))
                .querySelector('span.type-string').innerText.replaceAll('"', '')

            const object = store.getters.activeCollection.find(c => c[idField] == id)
            store.commit('setActiveObjectId', id)
            store.commit('setActiveObject', object)

            // clicked delete icon
            let clickedDelete = e.target.classList[0] === 'delete-x'
            setTimeout(() => {
                clickedDelete && store.commit('setModal', {
                    isOpen: true,
                    header: 'Delete <br>' + id
                })
            }, 0);

            // edit field
            // const field = e.target.closest('span')
            // if(field && field.classList[0].includes('type')) {
            //     console.log(field);

            //     const preField = field

            //     field.innerHTML = `<input style="display: inline-block; height: 16px; margin: 0; padding: 0 5px;" value="${preField.innerText.replaceAll('"', '')}">`

            // }
        }

        const importJSON = async () => {
            errorMessage.value = ''
            isImporting.value = true

            if(jsonFilesNotLoaded.value) {
                setTimeout(() => {
                    isImporting.value = false
                }, 150)
                return
            }

            let formData = new FormData();

            for(let file of jsonFile.value.files) {
               formData.append('files', file, file.name);
            }
            
            let res = await fetch('/rest/' + store.state.activeColl, {
               method: 'POST',
               body: formData
            });

            isImporting.value = false
            
            if(res.status == 500) {
                errorMessage.value = await res.text()
            } else {
                location.reload()
            }
                
            console.log(res, await res.text());
        }

        const openDropCollModal = () => {
            store.commit('setModal', {
                isOpen: true,
                header: 'Drop ' + store.state.activeColl
            })
        }

        const exportJSON = async () => {
            let blob = await fetch('/api/export-collection/' + store.state.activeColl)
            blob = await blob.blob()
            let url = URL.createObjectURL(blob);
            let a = document.createElement('a');
            a.href = url;
            a.download = store.state.activeColl + '.json';
            document.body.appendChild(a); // we need to append the element to the dom -> otherwise it will not work in firefox
            a.click();    
            a.remove();  //afterwards we remove the element again   
            URL.revokeObjectURL(url); // prevent memory leak
        }

        return {
            activeColl,
            jsonDiv,
            jsonFile,
            jsonClick,
            importJSON,
            exportJSON,
            fetchingColls,
            errorMessage,
            isImporting,
            jsonFilesNotLoaded,
            filesLoaded,
            openDropCollModal
        }
    }
}