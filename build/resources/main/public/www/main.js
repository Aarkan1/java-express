import App from './src/app.js'
import store from './src/store.js'

const { createApp } = Vue
const app = createApp(App)
app.use(store)
app.mount('#app')
