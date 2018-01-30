import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class Inventory {

	Map<String, Map<String,BigDecimal>> itemsMap;
	BigDecimal profit=null;
	
	public Inventory() {
		if(itemsMap==null)
			itemsMap= new TreeMap<String,Map<String,BigDecimal>>();
		profit= new BigDecimal("0");
	}
	
	public static void main(String[] args) {
		System.out.println("--------------------Inventory Management------------------");
		Inventory invM= new Inventory();
		while(true) {
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			if(input!=null && input.equalsIgnoreCase("exit")) {
				System.exit(0);
			} else {
				String[] cmd= input.split(" ");
				if(cmd.length>0) {
					try{
						if(cmd[0]!=null && cmd[0].equals("create")) {
							BigDecimal cp= new BigDecimal(cmd[2]);
							BigDecimal sp= new BigDecimal(cmd[3]);
							invM.create(cmd[1], cp, sp);
						}
						else if(cmd[0]!=null && cmd[0].equals("updateBuy")) {
							Integer qt= Integer.valueOf(cmd[2]);
							invM.updateBuy(cmd[1], qt);
						}
						else if(cmd[0]!=null && cmd[0].equals("updateSell")) {
							Integer qt= Integer.valueOf(cmd[2]);
							invM.updateSell(cmd[1], qt);
						}
						else if(cmd[0]!=null && cmd[0].equals("delete")) {
							invM.delete(cmd[1]);
						}
						else if(cmd[0]!=null && cmd[0].equals("report")) {
							invM.report();
						}
						else if(cmd[0]!=null && cmd[0].equals("updateSellPrice")) {
							BigDecimal sp= new BigDecimal(cmd[2]);
							invM.updateSellPrice(cmd[1], sp);
						}
						else {
							System.out.println("Not valid..!");
						}
					} catch(ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
						System.out.println("Not valid no of inputs..!");
					}
				}
			}
		}
	}
	
	private void create(String itemName, BigDecimal cp, BigDecimal sp) {
			
		Map<String,BigDecimal> itemDtls= new HashMap<String,BigDecimal>();
		itemDtls.put("CP", cp);
		itemDtls.put("SP", sp);
		itemDtls.put("QT", BigDecimal.ZERO);
		itemsMap.put(itemName, itemDtls);
	}
	
	private void delete(String itemName) {
		if(itemsMap!=null)
			itemsMap.remove(itemName);
	}
	
	private void updateBuy(String itemName, Integer quantity) {
		Map<String,BigDecimal> itemDtls= itemsMap.get(itemName);
		if(itemDtls!=null) {
			BigDecimal qt= (BigDecimal) itemDtls.get("QT");
			qt= qt.add(BigDecimal.valueOf(quantity));
			itemDtls.put("QT", qt);
		} else {
			System.out.println("Item does not exist in Inventory !!!");
		}
	}
	
	private void updateSell(String itemName, Integer quantity) {
		Map<String,BigDecimal> itemDtls= itemsMap.get(itemName);
		if(itemDtls!=null) {
			BigDecimal qt= (BigDecimal) itemDtls.get("QT");
			qt= qt.subtract(BigDecimal.valueOf(quantity));
			itemDtls.put("QT", qt);
			calcProfit(itemDtls, quantity);
			
		} else {
			System.out.println("Item does not exist in Inventory !!!");
		}
	}
	
	private void updateSellPrice(String itemName, BigDecimal sp) {
		Map<String,BigDecimal> itemDtls= itemsMap.get(itemName);
		if(itemDtls!=null) {
			itemDtls.put("SP",sp);
		}
	}
	
	private void report() {
		System.out.println("Item name        Bought At        Sold At        Avlbl Qty        Value");
		System.out.println("------------------------------------------------------------------------");
		System.out.println();
		BigDecimal totVal= new BigDecimal("0");
		Iterator<String> it= itemsMap.keySet().iterator();
		while(it.hasNext()){
			String itemName= it.next();
			Map dtls= (HashMap) itemsMap.get(itemName);
			BigDecimal cp= (BigDecimal) dtls.get("CP");
			BigDecimal sp= (BigDecimal) dtls.get("SP");
			BigDecimal qt= (BigDecimal) dtls.get("QT");
			BigDecimal val= qt.multiply(cp);
			totVal= totVal.add(val);
			System.out.print(itemName);
			System.out.print("           ");
			System.out.print(dtls.get("CP"));
			System.out.print("           ");
			System.out.print(dtls.get("SP"));
			System.out.print("           ");
			System.out.print(dtls.get("QT"));
			System.out.print("           ");
			System.out.println(qt.multiply(cp));
		}
		System.out.println("------------------------------------------------------------------------");
		System.out.println("Total value                                                  "+totVal);
		
		System.out.println("Profit since previous report: "+this.profit);
		
		this.profit= new BigDecimal("0");
	}
	
	private void calcProfit(Map<String, BigDecimal> itemDtls, Integer qt) {
		BigDecimal cp= (BigDecimal) itemDtls.get("CP");
		BigDecimal sp= (BigDecimal) itemDtls.get("SP");
		BigDecimal thisProfit= sp.subtract(cp).multiply(BigDecimal.valueOf(qt));		
		this.profit= this.profit.add(thisProfit);
	}
}
