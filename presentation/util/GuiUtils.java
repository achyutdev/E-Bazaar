package presentation.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import business.externalinterfaces.OrderItem;
import business.ordersubsystem.OrderItemImpl;
import presentation.data.*;

public class GuiUtils {
	
	public static String formatPrice(double d) {
		return String.format("%.2f", d);
	}
	
	public static double computeTotalInTable(TableView table) {
		ObservableList<CartItemPres> items = table.getItems();
		DoubleSummaryStatistics summary 
		  = items.stream().collect(
		       Collectors.summarizingDouble(item -> Double.parseDouble(item.totalPriceProperty().get())));
		return summary.getSum();
	}
	

	/** Assumes num1 num2 are String versions of doubles */
	public static String stringDoublesMultiply(String num1, String num2) {
		double d1 = Double.parseDouble(num1);
		double d2 = Double.parseDouble(num2);
		return (new Double(d1*d2)).toString();
	}

	public static StringProperty multiplyStringProps(StringProperty num1, StringProperty num2) {
		String retVal = stringDoublesMultiply(num1.get(), num2.get());
		return new SimpleStringProperty(retVal);
	}

	public static List<String> emptyStrings(int len) {
		List<String> eStrings = new ArrayList<>();
		for(int i = 0; i < len; ++i) {
			eStrings.add("");
		}
		return eStrings;
	}
	public SimpleStringProperty intToString(ObservableNumberValue val) {
		return new SimpleStringProperty((new Integer(val.intValue()).toString()));
		
	}
	public SimpleStringProperty doubleToString(ObservableNumberValue val) {
		return new SimpleStringProperty((new Double(val.doubleValue()).toString()));
		
	}
	public ObservableNumberValue toDouble(SimpleStringProperty p) {
		if(p != null && p.get() != null)
			return new SimpleDoubleProperty(Double.parseDouble(p.get()));
		return null;
	}
	public ObservableNumberValue toInteger(SimpleStringProperty p) {
		if(p != null && p.get() != null)
			return new SimpleDoubleProperty(Integer.parseInt(p.get()));
		return null;
	}
	public static  ObservableList<OrderItemPres> orderItemsToOrderItemsPres(List<OrderItem> list) {
		return FXCollections.observableList(list.stream()
                .map(orderItem -> {OrderItemPres p = new OrderItemPres();
                                   p.setOrderItem(orderItem); return p;})
                .collect(Collectors.toList()));
	}

}
