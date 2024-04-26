

import Ajax from "@common/Request";

export default {
	save(param) {
		return Ajax[param.id ? 'put' : 'post']('/userAccount', param)
	},
	list(param) {
		return Ajax.get('/userAccount', param)
	},
	remove(id) {
		return Ajax.delete('/userAccount/' + id);
	},
	select() {
		return Ajax.get('/userAccount/select');
	}
}
