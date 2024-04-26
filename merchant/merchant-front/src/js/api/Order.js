

import Ajax from "@common/Request";

export default {
	save(param) {
		return Ajax[param.id ? 'put' : 'post']('/order', param)
	},
	list(param) {
		return Ajax.get('/order', param)
	},
	remove(id) {
		return Ajax.delete('/order/' + id);
	},
	select() {
		return Ajax.get('/order/select');
	}
}
