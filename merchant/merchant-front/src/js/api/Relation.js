import Ajax from "@common/Request";

export default {

    init(param) {
        return Ajax.get('/relation/init', param)
    },
}