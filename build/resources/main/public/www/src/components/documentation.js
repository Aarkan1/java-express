const { ref, onMounted, onUnmounted } = Vue
const { useStore } = Vuex

export default {
    name: 'Documentation',
    setup() {
        const store = useStore()
        const docs = ref('')

        const getDocs = async () => {
            let doc = await fetch('/rest/docs')
            docs.value = await doc.text()
        }

        onMounted(async () => {
            store.commit('setShowDocumentAnchors', true)
            await getDocs()
            hljs.initHighlightingOnLoad()
        })

        onUnmounted(() => store.commit('setShowDocumentAnchors', false))

        return {
            docs
        }
    },
    template: `
        <div v-html="docs"></div>
    `
}