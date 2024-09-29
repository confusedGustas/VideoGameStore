import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

const app = createApp(App)

app.config.errorHandler = (err, vm, info) => {
    console.error('Global error:', err, info)
}

app.use(store)
app.use(router)
app.mount('#app')