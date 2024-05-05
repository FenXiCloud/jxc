
import Ajax from "@common/Request";

export default {
  list(param) {
    return Ajax.get('/checkout', param)
  },
  check(param) {
    return Ajax.post('/checkout/check', param)
  },
  update(param) {
    return Ajax.put('/checkout', param)
  },
}
