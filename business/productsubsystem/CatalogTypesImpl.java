package business.productsubsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import business.externalinterfaces.Catalog;
import business.externalinterfaces.CatalogTypes;

/**
 * @author pcorazza
 * <p>
 * Class Description: This is a bean...it just holds
 * data in memory. It is not the entity class for a catalog --
 * it is just reference data, stored in memory.
 */
public class CatalogTypesImpl implements CatalogTypes {
    HashMap<Integer,String> catalogIdToName = new HashMap<Integer,String>();
    HashMap<String,Integer> catalogNameToId = new HashMap<String,Integer>();
    List<Catalog> catalogs = new ArrayList<Catalog>();

    public List<String> getCatalogNames() {
      	String[] names = catalogIdToName.values().toArray(new String[0]);
    	return Arrays.asList(names);    
    }
    
    public List<Catalog> getCatalogs() {
    	return catalogs;
    }
    
    public String getCatalogName(Integer id){
        return catalogIdToName.get(id);
    }

    public void addCatalog(Integer id, String name) {
        catalogIdToName.put(id,name);
        catalogNameToId.put(name,id);
        catalogs.add(new CatalogImpl(id, name));
        
    }
    public Integer getCatalogId(String name) {
        return catalogNameToId.get(name);
        
        
    }
	
}
