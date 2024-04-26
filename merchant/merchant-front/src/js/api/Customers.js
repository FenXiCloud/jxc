

import Ajax from "@common/Request";

export default {
	save(param) {
		return Ajax[param.id ? 'put' : 'post']('/customers', param)
	},
	list(param) {
		return Ajax.get('/customers', param)
	},
	remove(id) {
		return Ajax.delete('/customers/' + id);
	},
	select() {
		return Ajax.get('/customers/select');
	}
}
