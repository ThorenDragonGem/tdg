// package tests.todelete;
//
// import tdg.game.Item;
// import tdg.game.ItemStack;
//
// public class InventoryTests
// {
// private ItemStack[] items;// TODO Item[][1]; or stacks
// private int slots;
//
// public InventoryTests(int slots)
// {
// items = new ItemStack[slots];
// this.slots = slots;
// }
//
// /**
// * If Inventory has an ItemStack of type of i
// * Then add +1 to this ItemStack
// * @param i
// */
// public void add(Item i)
// {
// // test before if a Stack with type of exists.
// System.out.println(this + " " + getNextFreeSlot());
// boolean added = false;
// for(ItemStack stack : items)
// {
// System.out.println(stack);
// if(stack == null || !stack.hasFreeSlots())
// continue;
// System.out.println("zizjej$zpiej po$k" + stack);
// if(stack.hasFreeSlots())
// {
// stack.addItem(i);
// added = true;
// }
// }
// if(added)
// return;
// int slot = createNewItemStack(i, getNextFreeSlot());
// items[slot].addItem(i);
// System.out.println(items[slot]);
// System.out.println(items[slot].hasFreeSlots() + " " + this.hasFreeSlot());
// added = true;
// if(added)
// return;
//
// //
// // boolean added = false;
// // if(getNextFreeSlot() != -1)
// // {
// // for(ItemStack stack : items)
// // {
// // if(stack == null && !added)
// // stack = new ItemStack(i, i.maxStackSize);
// // if(i.getClass() == stack.getItemType().getClass())
// // {
// // stack.addItem(i);
// // added = true;
// // System.out.println(stack);
// // }
// // }
// // if(!added)
// // items[getNextFreeSlot()] = new ItemStack(i, i.maxStackSize);
// // }
// }
//
// private int createNewItemStack(Item type, int nextSlot)
// {
// items[nextSlot] = new ItemStack(type, type.maxStackSize);
// return nextSlot;
// }
//
// // TODO
// // public void add(Item i, int index)
// // {
// // if(items[index].getItemType().getClass() == i.getClass())
// // {
// // items[index].addItem(i);
// // }
// //
// // if(items[index] == i)
// // return;
// // items[index] = i;
// // }
//
// public void removeItem(Item i)
// {
// for(ItemStack stack : items)
// {
// if(stack.getItemType().getClass() == i.getClass())
// stack.removeItem(i);
// }
// }
//
// public void removeStack(int index)
// {
// items[index] = null;
// }
//
// public void clearInventory()
// {
// for(int i = 0; i < items.length; i++)
// items[i] = null;
// }
//
// public int getNextFreeSlot()
// {
// for(int i = 0; i < items.length; i++)
// {
// if(items[i] == null)
// return i;
// }
// return -1;
// }
//
// public boolean hasFreeSlot()
// {
// if(getNextFreeSlot() != -1)
// return true;
// else
// return false;
// }
//
// public int slots()
// {
// return slots;
// }
//
// /**
// * Modifies the slot of the inventory.
// * If newSlots < lastSlots => items with index > newSlots disapears.
// * else, creates new empty slots;
// * @param slots
// */
// // public void setSlots(int slots)
// // {
// // Item[] items2 = items;
// // items = new Item[slots];
// // for(int i = 0; i < items.length; i++)
// // items[i] = items2[i];
// // this.slots = slots;
// // items2 = null;
// // }
//
// public Item getItem(int index)
// {
// return items[index].getItemType();
// }
//
// public int getStackIndex(Item item)
// {
// for(int i = 0; i < items.length; i++)
// {
// if(items[i].getItemType().getClass() == item.getClass())
// return i;
// }
// return -1;
// // for(int i = 0; i < items.length; i++)
// // if(items[i] == item)
// // return i;
// // return -1;
// }
//
// public ItemStack[] getList()
// {
// return items;
// }
//
// @Override
// public String toString()
// {
// String string = "";
// for(int i = 0; i < items.length; i++)
// {
// if(items[i] == null)
// string += "null ; ";
// else
// string += "(" + items[i].toString() + ") ; ";
//
// }
// string = string.substring(0, string.length() - 3);
// return string;
// }
//
// public void input()
// {
// for(ItemStack stack : items)
// if(stack != null)
// stack.getItemType().invInput();
// }
//
// public void update()
// {
// for(ItemStack stack : items)
// if(stack != null)
// stack.getItemType().invUpdate();
// }
// }
