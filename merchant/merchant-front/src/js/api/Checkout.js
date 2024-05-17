
import Ajax from "@common/Request";

export default {
  list(param) {
    return Ajax.get('/checkout', param)
  },
  toCheck(param) {
    return Ajax.post('/checkout', param)
  },
  antiCheckout(param) {
    return Ajax.put('/checkout', param)
  },
}
