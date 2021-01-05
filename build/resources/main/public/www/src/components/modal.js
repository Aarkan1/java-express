const { useStore } = Vuex
const { computed } = Vue

export default {
    name: 'Modal',
    template: `
        <div class="modal-backdrop" :style="{ display: modal.isOpen ? 'block' : 'none' }"></div>
        <dialog id="dialog" :open="modal.isOpen">
            <header v-html="modal.header"></header>
            <form method="dialog">
                <menu>
                    <button @click="closeModal">Cancel</button>
                    <button @click="deleteObject" class="button-warn">Confirm</button>
                </menu>
            </form>
        </dialog>
    `,
    setup() {
        const store = useStore()
        const id = computed(() => store.state.activeObjectId)
        const modal = computed(() => store.state.openModal)

        const closeModal = () => {
            store.commit('setModal', {
                isOpen: false,
                header: ''
            })
        }
        
        const deleteObject = async () => {
            if(modal.value.header.startsWith('Drop')) {
                let res = await fetch('/api/drop-collection/' + store.state.activeColl, {
                    method: 'DELETE'
                })
                console.log('dropping coll:', store.state.activeColl, await res.text());
                // history.pushState({ url: '/' }, '', '/')
                location.reload()
            } else {
                console.log('delete object:', id.value);
                store.commit('deleteObject', id.value)
            }
            closeModal()
        }

        return {
            id,
            deleteObject,
            closeModal,
            modal,
        }
    }
}