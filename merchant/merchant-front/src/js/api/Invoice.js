

import Ajax from "@common/Request";

export default {
	save(param) {
		return Ajax[param.id ? 'put' : 'post']('/invoice', param)
	},
	list(param) {
		return Ajax.get('/invoice', param)
	},
	remove(id) {
		return Ajax.delete('/invoice/' + id);
	},
	select() {
		return Ajax.get('/invoice/select');
	}
}
