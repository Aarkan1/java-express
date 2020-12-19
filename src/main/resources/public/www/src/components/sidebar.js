const { ref, computed } = Vue
const { useStore } = Vuex

export default {
    template: `
        <aside>
            <div>
                <button @click="refresh">Refresh</button>
                <slot></slot>
                <div v-if="props.displayCollections">
                    <a @click.prevent="setActiveColl(coll)" v-for="coll of colls" :key="coll">{{ coll }}</a>
                </div>
            </div>
            
            <div style="margin-left: 20px; margin-top: 10px;">
                Theme
                <label class="switch" @click.stop="toggleTheme">
                    <input v-model="isLightTheme" type="checkbox">
                    <span class="slider round"></span>
                </label>
                {{ isLightTheme ? 'Light' : 'Dark' }}
            </div>
        </aside>
    `,
    props: ['displayCollections'],
    setup(props) {
        const store = useStore()
        const colls = computed(() => store.state.collNames)
        const setActiveColl = coll => store.commit('setActiveColl', coll)

        const isLightTheme = ref(localStorage['colorTheme'] == 'light')

        const toggleTheme = () => {
            setTimeout(() => { 
                const lightCSS = document.querySelector('link[href*="light.min.css"]')
                const darkCSS = document.querySelector('link[href*="dark.min.css"]')
                const jsonLightCSS = document.querySelector('link[href*="viewer-light.css"]')
                const jsonDarkCSS = document.querySelector('link[href*="viewer-dark.css"]')
                
                lightCSS.setAttribute('rel', (localStorage['colorTheme'] == 'light') ? 'stylesheet' : 'stylesheet alternate')
                darkCSS.setAttribute('rel', (localStorage['colorTheme'] == 'light') ? 'stylesheet alternate' : 'stylesheet')
                jsonLightCSS.setAttribute('rel', (localStorage['colorTheme'] == 'light') ? 'stylesheet' : 'stylesheet alternate')
                jsonDarkCSS.setAttribute('rel', (localStorage['colorTheme'] == 'light') ? 'stylesheet alternate' : 'stylesheet')
                
                localStorage['colorTheme'] = isLightTheme.value ? 'light' : 'dark'
            }, 0);
        }

        const refresh = () => {
            history.pushState({ url: '/' }, '', '/')
            location.reload()
        }

        return {
            isLightTheme,
            toggleTheme,
            colls,
            setActiveColl,
            props,
            refresh
        }
    }
}