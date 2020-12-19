const { watch, computed, onMounted, onBeforeUnmount, ref } = Vue
const { useStore } = Vuex

export default {
    name: 'Collections',
    template: `
        <div>
            <h2>{{ activeColl }} Collection</h2>
            <div v-if="fetchingColls" class="loader">Loading...</div>
            <div v-show="!fetchingColls" @click="jsonClick" ref="jsonDiv" id="json"></div>
        </div>
    `,
    setup() {
        const store = useStore()
        const jsonDiv = ref()
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
        
        // rerender on collection change
        watch([activeColl, collection], () => {
            renderJson(store.getters.activeCollection)
        })
        
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
                clickedDelete && store.commit('setOpenModal', true)
            }, 0);

            // edit field
            // const field = e.target.closest('span')
            // if(field && field.classList[0].includes('type')) {
            //     console.log(field);

            //     const preField = field

            //     field.innerHTML = `<input style="display: inline-block; height: 16px; margin: 0; padding: 0 5px;" value="${preField.innerText.replaceAll('"', '')}">`

            // }
        }

        return {
            activeColl,
            jsonDiv,
            jsonClick,
            fetchingColls
        }
    }
}