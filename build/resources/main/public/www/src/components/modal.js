const { useStore } = Vuex
const { computed } = Vue

export default {
    name: 'Documentation',
    template: `
        <div class="modal-backdrop" :style="{ display: isOpen ? 'block' : 'none' }"></div>
        <dialog id="dialog" :open="isOpen">
            <header>Delete<br>{{ id }}</header>
            <form method="dialog">
                <menu>
                    <button @click="closeModal">Cancel</button>
                    <button @click="deleteObject">Confirm</button>
                </menu>
            </form>
        </dialog>
    `,
    setup() {
        const store = useStore()

        const id = computed(() => store.state.activeObjectId)
        const isOpen = computed(() => store.state.openModal)

        const closeModal = () => {
            store.commit('setOpenModal', false)
        }
        
        const deleteObject = () => {
            console.log('delete object:', id.value);
            store.commit('deleteObject', id.value)
            closeModal()
        }

        return {
            id,
            deleteObject,
            closeModal,
            isOpen
        }
    }
}