import Ajax from "@common/Request";

export default {
    loadAccountSets(param) {
        return Ajax.get('/relationCw/loadAccountSets', param)
    },
    save(param) {
        return Ajax.post('/relationCw', param)
    },
    load(){
        return Ajax.get('/relationCw')
    }
}