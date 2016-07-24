package presentation.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.Address;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.Product;
import business.ordersubsystem.OrderImpl;
import business.ordersubsystem.OrderItemImpl;
import business.productsubsystem.ProductSubsystemFacade;

public class DefaultData {
	public static Catalog BOOKS_CATALOG;
	public static Catalog CLOTHES_CATALOG;
	public static final CatalogPres BOOKS_CAT_PRES = new CatalogPres();
	public static final CatalogPres CLOTHES_CAT_PRES = new CatalogPres();
	public static final int DEFAULT_QUANTITY_AVAILABLE = 4;
	static {
		BOOKS_CATALOG = ProductSubsystemFacade.createCatalog(1,  "Books");
		CLOTHES_CATALOG = ProductSubsystemFacade.createCatalog(2,  "Clothes");
		BOOKS_CAT_PRES.setCatalog(BOOKS_CATALOG);
		CLOTHES_CAT_PRES.setCatalog(CLOTHES_CATALOG);
	}
	public static final HashMap<String, Catalog> CATALOG_MAP = new HashMap<String, Catalog>() {
		{
			put(BOOKS_CATALOG.getName(), BOOKS_CATALOG);
			put(CLOTHES_CATALOG.getName(), CLOTHES_CATALOG);
		}
	};

	public static final Product MESSIAH_BOOK
	  = ProductSubsystemFacade.createProduct(BOOKS_CATALOG,"Messiah Of Dune", LocalDate.of(2000, 11, 11), 20, 15.00);
	public static final Product GONE_BOOK
	  =  ProductSubsystemFacade.createProduct(BOOKS_CATALOG,"Gone with the Wind", LocalDate.of(1995, 12, 5), 15, 12.00);
	public static final Product GARDEN_BOOK
	  = ProductSubsystemFacade.createProduct(BOOKS_CATALOG,"Garden of Rama", LocalDate.of(2005, 1, 1), 5, 18.00);
	public static final Product PANTS
	  = ProductSubsystemFacade.createProduct(CLOTHES_CATALOG, "Pants", LocalDate.of(2000, 11, 1), 20, 15.00);
	public static final Product SKIRTS
	  = ProductSubsystemFacade.createProduct(CLOTHES_CATALOG, "Skirts", LocalDate.of(1995, 1, 5), 15, 12.00);
	public static final Product TSHIRTS
	  = ProductSubsystemFacade.createProduct(CLOTHES_CATALOG, "T-Shirts", LocalDate.of(2003, 6, 18), 10, 22.00);
	public static final HashMap<String, Product> PRODNAME_TO_PRODUCT = new HashMap<String, Product>() {
		{
			put("Messiah Of Dune",MESSIAH_BOOK);
			put("Gone with the Wind",GONE_BOOK);
			put("Garden of Rama",GARDEN_BOOK);
			put("Pants", PANTS);
			put("Skirts",SKIRTS);
			put("T-Shirts",TSHIRTS);

		}
	};
	public static final ProductPres MESSIAH_BOOK_PRES = new ProductPres();
	public static final ProductPres GONE_BOOK_PRES = new ProductPres();
	public static final ProductPres GARDEN_BOOK_PRES = new ProductPres();
	public static final ProductPres PANTS_PRES = new ProductPres();
	public static final ProductPres SKIRTS_PRES = new ProductPres();
	public static final ProductPres TSHIRTS_PRES = new ProductPres();


	public static final List<CatalogPres> CATALOG_LIST_DATA = new ArrayList<CatalogPres>() {
		{
			add(BOOKS_CAT_PRES);
			add(CLOTHES_CAT_PRES);
		}
	};

	public final static ObservableMap<CatalogPres, List<ProductPres>> PRODUCT_LIST_DATA =
            FXCollections.observableHashMap();

	public static final List<String> DEFAULT_SHIP_DATA
	   = Arrays.asList("101 Adams St.(nabinChange)", "Fairfield", "IA", "52556");
	public static final List<String> DEFAULT_BILLING_DATA
	   = Arrays.asList("21 Berkeley Ave.", "Cincinnati", "OH", "45201");
	public static final List<String> DEFAULT_PAYMENT_INFO
		= Arrays.asList("John Thomas", "5555444466669999", "Visa", "11/11/2017");
	//"Name", "Card Number", "Card Type", "Expiration Date"
	public static final List<Address> ADDRESSES_ON_FILE
	   = Arrays.asList(CustomerSubsystemFacade.createAddress("101 Jackson St", "Fairfield", "IA", "52556", true, true),
			   CustomerSubsystemFacade.createAddress("300 W. Washington Ave", "Fairfield", "IA", "52556", true, false),
			   CustomerSubsystemFacade.createAddress("1000 N. 4th St.", "Fairfield", "IA", "52557", false, true),
			   CustomerSubsystemFacade.createAddress("1435 Channing Ave", "Palo Alto", "CA", "94301", true, true));
	public static final List<CustomerPres> CUSTS_ON_FILE
		= Arrays.asList(new CustomerPres(CustomerSubsystemFacade.createCustProfile(1, "John", "Smith", false)),
				new CustomerPres(CustomerSubsystemFacade.createCustProfile(2, "Andrew", "Anderson", false)),
				new CustomerPres(CustomerSubsystemFacade.createCustProfile(3, "Ralph", "Horowitz", false)),
				new CustomerPres(CustomerSubsystemFacade.createCustProfile(4, "Donald", "Knuth", false)));

	public static final String DEFAULT_CARD_TYPE = "Visa";
	public static final CartItemData savedItem1 = new CartItemData();
	public static final CartItemData savedItem2 = new CartItemData();
	public static final CartItemPres savedItemPres1 = new CartItemPres();
	public static final CartItemPres savedItemPres2 = new CartItemPres();

	public static final List<CartItemPres> savedCartItems = new ArrayList<>();
	//String name, String quantity, String price
	public static final OrderItemImpl orderItem1 = new OrderItemImpl(MESSIAH_BOOK.getProductName(),
			2, MESSIAH_BOOK.getUnitPrice());
	public static final OrderItemImpl orderItem2 = new OrderItemImpl(GARDEN_BOOK.getProductName(),
			3, GARDEN_BOOK.getUnitPrice());
	public static final OrderItemImpl orderItem3 = new OrderItemImpl(PANTS.getProductName(),
			2, PANTS.getUnitPrice());
	public static final OrderItemImpl orderItem4 = new OrderItemImpl(SKIRTS.getProductName(),
			1, SKIRTS.getUnitPrice());
	public static final OrderImpl order1 = new OrderImpl();
	public static final OrderPres orderPres1 = new OrderPres();
	public static final OrderImpl order2 = new OrderImpl();
	public static final OrderPres orderPres2 = new OrderPres();
	public static final List<OrderPres> ALL_ORDERS = Arrays.asList(orderPres1, orderPres2);

	static {
		orderPres1.setOrder(order1);
		orderPres2.setOrder(order2);
		savedItemPres1.setCartItem(savedItem1);
		savedItemPres2.setCartItem(savedItem2);
		savedCartItems.add(savedItemPres1);
		savedCartItems.add(savedItemPres2);
		orderItem1.setOrderItemId(1001);
		orderItem2.setOrderItemId(1002);
		orderItem3.setOrderItemId(1003);
		orderItem4.setOrderItemId(1004);
		orderItem1.setOrderId(101);
		orderItem2.setOrderId(101);
		orderItem3.setOrderId(102);
		orderItem4.setOrderId(102);
		order1.setOrderId(10);
		order2.setOrderId(11);
		order1.setDate(LocalDate.of(2014, 11, 11));
		order2.setDate(LocalDate.of(2015, 2, 5));
		order1.setOrderItems(Arrays.asList(orderItem1, orderItem2));
		order2.setOrderItems(Arrays.asList(orderItem3, orderItem4));

		MESSIAH_BOOK.setDescription("You saw how good Dune was. \nThis is Part 2 of this \nunforgettable trilogy.");
		GONE_BOOK.setDescription("A moving classic that tells \na tale of love and \na tale of courage.");
		GARDEN_BOOK.setDescription("Highly acclaimed Book \nof Isaac Asimov. A real \nnail-biter.");
		PANTS.setDescription("I've seen the Grand Canyon. I've camped \nat Yellowstone. But nothing on \nearth compares to the look and feel of \nthese pants.");
		SKIRTS.setDescription("Once this brand of skirts \nbecomes well-known, watch out!");
		TSHIRTS.setDescription("Can be worn by men or women. \nAlways in style.");
		MESSIAH_BOOK.setProductId(11);
		GONE_BOOK.setProductId(12);
		GARDEN_BOOK.setProductId(13);
		PANTS.setProductId(14);
		SKIRTS.setProductId(15);
		TSHIRTS.setProductId(16);
		MESSIAH_BOOK_PRES.setProduct(MESSIAH_BOOK);
		GONE_BOOK_PRES.setProduct(GONE_BOOK);
		GARDEN_BOOK_PRES.setProduct(GARDEN_BOOK);
		PANTS_PRES.setProduct(PANTS);
		SKIRTS_PRES.setProduct(SKIRTS);
		TSHIRTS_PRES.setProduct(TSHIRTS);

		savedItem1.setItemName(MESSIAH_BOOK.getProductName());
		savedItem1.setPrice(MESSIAH_BOOK.getUnitPrice());
		savedItem1.setQuantity(2);
		//savedItem1.setTotalPrice(savedItem1.getQuantity() * savedItem1.getPrice());
		savedItem2.setItemName(PANTS.getProductName());
		savedItem2.setPrice(PANTS.getUnitPrice());
		savedItem2.setQuantity(3);
		//savedItem2.setTotalPrice(GuiUtils.stringDoublesMultiply(savedItem2.getQuantity(), savedItem2.getPrice()));

		PRODUCT_LIST_DATA.put(BOOKS_CAT_PRES,
				new ArrayList<ProductPres>() {
					{
						add(MESSIAH_BOOK_PRES);
						add(GONE_BOOK_PRES);
						add(GARDEN_BOOK_PRES);
					}
				});


		PRODUCT_LIST_DATA.put(CLOTHES_CAT_PRES,
				new ArrayList<ProductPres>() {
				{
					add(PANTS_PRES);
					add(SKIRTS_PRES);
					add(TSHIRTS_PRES);
				}
		});

		for(int i = 0; i < 4; ++i) {
			CUSTS_ON_FILE.get(i).setAddress(ADDRESSES_ON_FILE.get(i));

		}


	}


	//produces a random int between x and x+80
	public static String generateId(int x) {
		Random rand = new Random();
		Integer next = x + rand.nextInt(80);
		return next.toString();
	}
}
