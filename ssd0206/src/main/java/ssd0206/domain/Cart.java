package ssd0206.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.support.PagedListHolder;

@SuppressWarnings("serial")
public class Cart implements Serializable {

  /* Private Fields */

	private final Map<Integer, CartItem> itemMap = Collections.synchronizedMap(new HashMap<Integer, CartItem>());
	
	private final PagedListHolder<CartItem> itemList = new PagedListHolder<CartItem>();

  /* JavaBeans Properties */

	public Cart() {
		this.itemList.setPageSize(4);
	}

	public Iterator<CartItem> getAllCartItems() { return itemList.getSource().iterator(); }
	public PagedListHolder<CartItem> getCartItemList() { return itemList; }
	public int getNumberOfItems() { return itemList.getSource().size(); }

  /* Public Methods */

	public boolean containsItemId(int workingItemId) {
		return itemMap.containsKey(workingItemId);
	}

	public void addItem(SalesItem item, boolean isInStock) {
		CartItem cartItem = itemMap.get(item.getItemId());
		if (cartItem == null) {
			cartItem = new CartItem();
			cartItem.setItem(item);
			cartItem.setQuantity(0);
			cartItem.setInStock(isInStock);
			itemMap.put(item.getItemId(), cartItem);
			itemList.getSource().add(cartItem);
		}
		cartItem.incrementQuantity();
  }


	public SalesItem removeItemById(int itemId) {
		CartItem cartItem = itemMap.remove(itemId);
		if (cartItem == null) {
			return null;
		}
		else {
			itemList.getSource().remove(cartItem);
			return cartItem.getItem();
		}
	}

	public void incrementQuantityByItemId(int workingItemId) {
		CartItem cartItem = itemMap.get(workingItemId);
		cartItem.incrementQuantity();
	}

	public void setQuantityByItemId(int itemId, int quantity) {
		CartItem cartItem = itemMap.get(itemId);
		cartItem.setQuantity(quantity);
	}

	public double getSubTotal() {
		double subTotal = 0;
		Iterator<CartItem> items = getAllCartItems();
		while (items.hasNext()) {
			CartItem cartItem = (CartItem) items.next();
			SalesItem item = cartItem.getItem();
			double listPrice = item.getListPrice();
			int quantity = cartItem.getQuantity();
			subTotal += listPrice * quantity;
		}
		return subTotal;
	}

}
