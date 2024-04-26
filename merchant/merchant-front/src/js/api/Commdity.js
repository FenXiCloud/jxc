

import Ajax from "@common/Request";

export default {
	save(param) {
		return Ajax[param.id ? 'put' : 'post']('/commdity', param)
	},
	list(param) {
		return Ajax.get('/commdity', param)
	},
	remove(id) {
		return Ajax.delete('/commdity/' + id);
	},
	select() {
		return Ajax.get('/commdity/select');
	}
}
