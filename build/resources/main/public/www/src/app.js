import sidebar from './components/sidebar.js'
import collections from './components/collections.js'
import modal from './components/modal.js'
import * as pages from './components/pages.js'

const { watchEffect, ref } = Vue
const { useStore } = Vuex

export default {
    components: Object.assign({ sidebar, collections, modal }, pages),
    template: `
        <modal />
        <header>
            <h1>Java Express - Browse Collections</h1>
        </header>
        <sidebar :displayCollections="displayCollections">
            <button v-for="item in pages" :key="item.name" @click="routeTo(item.name)">{{ item.name }}</button>
            <ul v-if="page == 'documentation'">
                <li><a href="#getting-started">Getting started</a></li>
                <li><a href="#collectionoptions">CollectionOptions</a></li>
                <li><a href="#import">Import</a></li>
                <li><a href="#export">Export</a></li>
                <li><a href="#drop">Drop</a></li>
                <ul>
                    <li><a href="#important-note">Important note!</a></li>
                </ul>
                <li><a href="#model">Model</a></li>
                <li><a href="#collection-methods">Collection methods</a></li>
                <ul>
                    <li><a href="#filters">Filters</a></li>
                    <li><a href="#findoptions">FindOptions</a></li>
                </ul>
                <li><a href="#examples">Examples</a></li>
            </ul>
            <button @click="routeTo()">Collections</button>
        </sidebar>
        <main>
        <component :is="page || 'collections'"></component>
        </main>
        <footer>&copy; 2020 Johan Wirén</footer>
    `,
    setup() {
        const store = useStore()

        store.dispatch('getCollNames')

        const page = ref(null)
        const displayCollections = ref(true)

        //url management
        watchEffect(() => {
            const urlpage = window.location.pathname.split("/").pop()
            
            if (page.value == null) {
                page.value = urlpage
            }
            if (page.value != urlpage) { 
                const url = page.value ? page.value : './'
                window.history.pushState({ url }, '', url) 
            }
            window.onpopstate = () => {
                page.value = window.location.pathname.split("/").pop()
            }
        })

        const routeTo = url => {
            if(!url) {
                page.value = ''
                displayCollections.value = true
            } else {   
                page.value = url.toLowerCase()
                displayCollections.value = false
            }
        }

        return {
            page, 
            pages, 
            displayCollections,
            routeTo
        }
    }
}