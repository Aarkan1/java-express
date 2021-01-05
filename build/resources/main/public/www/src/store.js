const { createStore } = Vuex

const state = {
    activeColl: '',
    collNames: [],
    collIdFields: {},
    collections: {},
    activeObjectId: '',
    activeObject: {},
    openModal: {
        isOpen: false,
        header: ''
    },
    openDropCollModal: false,
    fetchingColls: false
}

const mutations = {
    setModal(state, modal) {
        state.openModal = modal
    },
    setFetchingColls(state, isFetching) {
        state.fetchingColls = isFetching
    },
    setActiveColl(state, coll) {
        state.activeColl = coll
        localStorage['activeColl'] = coll
    },
    setActiveObject(state, object) {
        state.activeObject = object
    },
    setActiveObjectId(state, id) {
        state.activeObjectId = id
    },
    setCollNames(state, colls) {
        state.collNames = colls
    },
    addCollection(state, data) {
        Object.assign(state.collections, { [data.coll]: data.collection })
        state.collections = {...state.collections}
    },
    addCollIdFields(state, data) {
        state.collIdFields[data.coll] = data.idField
    },
    deleteObject(state, id) {
        state.collections[state.activeColl] = state.collections[state.activeColl].filter(coll =>  coll[state.collIdFields[state.activeColl]] != id)
        state.collections = {...state.collections}
        fetch(`/rest/${state.activeColl}/${id}`, { method: 'DELETE' })
    }
}

const actions = {
    async getCollNames(store) {
        store.commit('setFetchingColls', true)
        let collNames = await fetch('/rest/collNames')
        collNames = await collNames.json();

        if(!collNames.includes(localStorage['activeColl'])) {
            localStorage['activeColl'] = collNames[0]
        }

        store.commit('setActiveColl', localStorage['activeColl'] || collNames[0])
        store.commit('setCollNames', collNames)

        for(let coll of collNames) {
            store.dispatch('getCollection', coll)
        }
    },
    async getCollection(store, coll) {
        let data = await fetch('/rest/' + coll)
        data = await data.json()
        const [idField, collection] = Object.entries(data)[0]
        
        store.commit('addCollIdFields', {coll, idField})
        store.commit('addCollection', {coll, collection})
        
        if(coll === store.state.activeColl || Object.keys(store.state.collections).length === store.state.collNames.length) {
            store.commit('setFetchingColls', false)
        }
    }
}

const getters = {
    activeCollection(state) {
        return state.collections[state.activeColl]
    },
    activeField(state) {
        return state.collIdFields[state.activeColl]
    }
}

export default createStore({state, mutations, actions, getters})
