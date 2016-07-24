package presentation.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

import presentation.data.Synchronizer;
import presentation.gui.EditingCell;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
public class TableUtil {

	public static <T> TableColumn<T, String> makeTableColumn(T underlyingClass, String colHeader, String nameOfProperty, int minWidth) {
		TableColumn<T, String> tableColumn 
		  = new TableColumn<>(colHeader);
		tableColumn.setMinWidth(minWidth);
		tableColumn.setCellValueFactory(celldata -> computeProperty(celldata.getValue(), nameOfProperty));
		tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		tableColumn.setEditable(false);
		return tableColumn;
	}
	
	private static <T> StringProperty computeProperty(T instance, String fieldname) {
		Class cl = instance.getClass();
		return getPropertyValue(fieldname, cl, instance);
	}
	
	public static <T> TableColumn<T, String> makeEditableTableColumn(
			TableView<T> table, T underlyingClass, String colHeader, String nameOfProperty, int minWidth) {
		Callback<TableColumn<T, String>, TableCell<T, String>> cellFactory 
		        = new Callback<TableColumn<T,String>, TableCell<T,String>>() {
			@Override
	        public TableCell<T, String> call(TableColumn<T, String> p) {
	            return new EditingCell("", underlyingClass, table);
	        }
	    };
		TableColumn<T, String> tableColumn 
		  = new TableColumn<>(colHeader);
		tableColumn.setMinWidth(minWidth);
		tableColumn.setCellValueFactory(
				celldata -> computeProperty(celldata.getValue(), nameOfProperty));
		tableColumn.setCellFactory(cellFactory);
		tableColumn.setEditable(true);
		/*
		tableColumn.setOnEditCommit(t -> {
			T instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
			Class<T> cl = (Class<T>)underlyingClass.getClass();
			
			setValue(field, cl, t.getNewValue(), instance);
			System.out.println("value set");
			//TableUtil.selectByRow(table);	
		}); */	
		return tableColumn;
	}
	
	
	/*
	public static <T> TableColumn<T, String> makeEditableTableColumn(TableView<T> table, T underlyingClass, String colHeader, String field, int minWidth) {
		Callback<TableColumn<T, String>, TableCell<T, String>> cellFactory 
		        = new Callback<TableColumn<T,String>, TableCell<T,String>>() {
			@Override
	        public TableCell<T, String> call(TableColumn<T, String> p) {
	            return new EditingCell(field, underlyingClass, table);
	        }
	    };
		TableColumn<T, String> tableColumn 
		  = new TableColumn<>(colHeader);
		tableColumn.setMinWidth(minWidth);
		tableColumn.setCellValueFactory(
             new PropertyValueFactory<T, String>(field));
		tableColumn.setCellFactory(cellFactory);
		tableColumn.setEditable(true);
		/*
		tableColumn.setOnEditCommit(t -> {
			T instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
			Class<T> cl = (Class<T>)underlyingClass.getClass();
			
			setValue(field, cl, t.getNewValue(), instance);
			System.out.println("value set");
			//TableUtil.selectByRow(table);	
		}); *//*	
		return tableColumn;
	}*/
	
	//not in use; shows how to make editable column and update another column
	public static <T> TableColumn<T, String> makeEditableTableColumnSideEffect(
			TableView<T> table, T underlyingClass, String colHeader, String field, int minWidth,
			   final BiFunction<String, String, String> f, String field2, String field3) {
		Callback<TableColumn<T, String>, TableCell<T, String>> cellFactory 
		        = new Callback<TableColumn<T,String>, TableCell<T,String>>() {
			@Override
	        public TableCell<T, String> call(TableColumn<T, String> p) {
	            return new EditingCell(field, underlyingClass, table);
	        }
	    };
		TableColumn<T, String> tableColumn 
		  = new TableColumn<>(colHeader);
		tableColumn.setMinWidth(minWidth);
		tableColumn.setCellValueFactory(
             new PropertyValueFactory<T, String>(field));
		tableColumn.setCellFactory(cellFactory);
		tableColumn.setEditable(true);
		
		tableColumn.setOnEditCommit(t -> {
			T instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
			Class<T> cl = (Class<T>)underlyingClass.getClass();
			
			//set value that was just changed by user
			setValue(field, cl, t.getNewValue(), instance);
			
			//compute side effect			
			String fieldVal2 = getValue(field2, cl, instance);		
			setValue(field3, cl, f.apply(t.getNewValue(), fieldVal2), instance);
					
			TableUtil.selectByRow(table);
			TableUtil.refreshTable(table);			
		}); 
		
		
		return tableColumn;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> String getValue(String field, Class cl, T instance) {
		String methodName = "get" + field.substring(0,1).toUpperCase() + field.substring(1);
		System.out.println(methodName);
		String retVal = null;
		try {
			
			Method method = cl.getDeclaredMethod(methodName);
			retVal = (String)method.invoke(instance);				
					
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return retVal;
		
	}
	private static <T> StringProperty getPropertyValue(String field, Class<T> cl, T instance) {
		String methodName = field;
		System.out.println(methodName);
		StringProperty retVal = null;
		try {
			
			Method method = cl.getDeclaredMethod(methodName);
			retVal = (StringProperty)method.invoke(instance);				
					
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return retVal;
		
	}
	@SuppressWarnings("unchecked")
	private static <T> void setValue(String field, Class<T> cl, String newValue, T instance) {
		String methodName = "set" + field.substring(0,1).toUpperCase() + field.substring(1);
		System.out.println(methodName);
		try {
			
			Method method = cl.getDeclaredMethod(methodName, String.class);
			method.invoke(instance, newValue);				
					
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	public static <T> void refreshTable(TableView<T> table, Synchronizer synch) {
		//Forces a refresh -- this is a workaround
		ObservableList<T> items = table.getItems();	
		List<T> copy = new ArrayList<>();
		//using FXCollections.copy keeps original list tied to new list
		for(T c : items) {
			copy.add(c);
		}	
		items.removeAll(items);
		ObservableList<T> newItems 
		  = FXCollections.observableList(copy);	
		table.setItems(newItems);
		//keep backing class in synch with table
		if(synch != null)
			synch.refresh(newItems);
	}
	//For internal use only; refreshTable must ordinarily call a Synchronizer
	//to ensure that refresh happens to the data source as well as to the tableview
	private static <T> void refreshTable(TableView<T> table) { 
		refreshTable(table, null);
	}
		
	
	public static <T> TableView.TableViewSelectionModel<T> selectByCell(TableView<T> table) {
		TableView.TableViewSelectionModel<T> selModel
			= table.getSelectionModel();
		selModel.setCellSelectionEnabled(true);
		return selModel;
	}
	public static <T> TableView.TableViewSelectionModel<T> selectByRow(TableView<T> table) {
		TableView.TableViewSelectionModel<T> selModel
			= table.getSelectionModel();
		selModel.setCellSelectionEnabled(false);
		return selModel;
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
}
