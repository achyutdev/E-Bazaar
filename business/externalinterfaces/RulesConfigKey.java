package business.externalinterfaces;

public enum RulesConfigKey {
	DIR_PREFIX("dir_prefix"),
	ADDRESS_MODULE("address_module"), 
	ADDRESS_RULES_FILE("address_rulesfile"),
	ADDRESS_DEFTEMPLATE("address_deftemplate"),
	PAYMENT_MODULE("payment_module"), 
	PAYMENT_RULES_FILE("payment_rulesfile"),
	PAYMENT_DEFTEMPLATE("payment_deftemplate"),
	QUANTITY_MODULE("quantity_module"), 
	QUANTITY_RULES_FILE("quantity_rulesfile"),
	QUANTITY_DEFTEMPLATE("quantity_deftemplate"),
	SHOPCART_MODULE("shopcart_module"), 
	SHOPCART_RULES_FILE("shopcart_rulesfile"),
	SHOPCART_DEFTEMPLATE("shopcart_deftemplate"),
	FINAL_ORDER_MODULE("finalorder_module"), 
	FINAL_ORDER_RULES_FILE("finalorder_rulesfile"),
	FINAL_ORDER_DEFTEMPLATE("finalorder_deftemplate");
	
	private String val;
	private RulesConfigKey(String val) {
		this.val = val;
	}
	public String getVal() {
		return val;
	}
	
}
