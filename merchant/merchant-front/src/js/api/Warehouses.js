
import Ajax from "@common/Request";

export default {
  save(param) {
    return Ajax[param.id ? 'put' : 'post']('/warehouses', param)
  },
  list(param) {
    return Ajax.get('/warehouses', param)
  },
  remove(id) {
    return Ajax.delete('/warehouses/' + id);
  },
  select(param) {
    return Ajax.get('/warehouses/select', param)
  },
}
