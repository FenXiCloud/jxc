import Ajax from "@common/Request";

export default {
    loadSubject(param) {
        return Ajax.get('/relationSubject/loadSubject', param)
    },
    save(param) {
        return Ajax.post('/relationSubject', param)
    },
    load(id){
        return Ajax.get('/relationSubject/load/'+id)
    }
}