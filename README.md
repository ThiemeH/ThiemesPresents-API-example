<h1 align="center">
  <br>
  <img src="https://raw.githubusercontent.com/ThiemeH/ThiemesPresents/master/docs/assets/header.png">
  <br>
</h1>


<h2 align="center">
 Thieme's Presents API Documentation
</h2>


Here's a few examples of how you could use **Thieme's Presents API**. All of these examples can be found in this repository. Feel free to clone it and play around yourself.

## Index

1. [Getting started](#Getting-started)
   1. [Intellij](#intellij)
   2. [Eclipse](#eclipse)
2. [Events](#Events)
3. [Custom Presents](#Custom-presents)
   1. [Present Properties](#present-properties)
   2. [Adding Custom Presents](#Adding-Custom-Presents)
4. [Support](#support)

## Getting started

First of all, make sure to add the jar file to your project.

### [Intellij](https://stackoverflow.com/a/1051705)

1. Click File from the toolbar
2. Select Project Structure option
3. Select Modules at the left panel
4. Select Dependencies tab
5. Select + icon
6. Select the jar and click on apply

### [Eclipse](https://www.geeksforgeeks.org/how-to-add-jar-file-to-classpath-in-java/)
1. Right-Click on your project name
2. Click on Build Path
3. Click on configure build path
4. Click on libraries and click on “Add External JARs”
5. Select the jar file from the folder where you have saved your jar file
6. Click on Apply and Ok.

Now you're all set!

---



## Events

The API contains the following events:

| Events                                                       | Description                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [PresentOpenEvent.Pre](#PresentOpenEvent.Pre) (Cancellable)  | Fired before a present is opened                             |
| [PresentOpenEvent.Post](#PresentOpenEvent.Post)              | Fired after a present is opened                              |
| [PresentSignEvent.Pre](#PresentSignEvent.Pre) (Cancellable)  | Fired when a player is about to sign (nothing has been sent in chat yet) |
| [PresentSignEvent.Post](#PresentSignEvent.Post) (Cancellable) | Fired when a message in chat was sent (before item is signed) |
| [PresentWrapEvent.Pre](#PresentWrapEvent.Pre) (Cancellable)  | Fired before a present is wrapped (item was selected and about to be wrapped) |
| [PresentWrapEvent.Post](#PresentWrapEvent.Post)              | Fired after item is wrapped                                  |

---



### PresentOpenEvent.Pre

---
Available methods:

| Method                           | Purpose                                                  |
| -------------------------------- | -------------------------------------------------------- |
| getPlayer(): Player              | Returns Player that tries to open the present            |
| getPresentItemStack(): ItemStack | Returns the ItemStack (gift/present) that will be opened |
| setCancelled(boolean): Void      | Cancel the event                                         |

Code example:


```java
		@EventHandler
    public void onPresentOpen(PresentOpenEvent.Pre e) {
        Player p = e.getPlayer();
        e.setCancelled(true);
        p.sendMessage("Nah. You don't get to open a present today");
    }
```



### PresentOpenEvent.Post
---
Available methods:

| Method                           | Purpose                                                  |
| -------------------------------- | -------------------------------------------------------- |
| getPlayer(): Player              | Returns Player that tries to open the present            |
| getPresentItemStack(): ItemStack | Returns the ItemStack (gift/present) that will be opened |



### PresentSignEvent.Pre

---
Available methods:

| Method                           | Purpose                                                  |
| -------------------------------- | -------------------------------------------------------- |
| getPlayer(): Player              | Returns the player that wants to sign the present        |
| getPresentItemStack(): ItemStack | Returns the ItemStack (gift/present) that will be signed |
| setCancelled(boolean): Void      | Cancel the event                                         |



### PresentSignEvent.Post

---

Available methods:

| Method                           | Purpose                                                  |
| -------------------------------- | -------------------------------------------------------- |
| getPlayer(): Player              | Returns the player that wants to sign the present        |
| getPresentItemStack(): ItemStack | Returns the ItemStack (gift/present) that will be signed |
| setCancelled(boolean): Void      | Cancel the event                                         |
| getMessage(): String             | Returns the message that will be written on the present  |

Code example:

```java
		@EventHandler
    public void onSign(PresentSignEvent.Post e) {
        e.setCancelled(true);
        e.getPlayer().sendMessage("You wrote: " + e.getMessage());
    }
```

Note: the item is not signed yet before this event

### PresentWrapEvent.Pre

---

Available methods:

| Method                           | Purpose                                          |
| -------------------------------- | ------------------------------------------------ |
| getPlayer(): Player              | Returns the player that is about to wrap an item |
| getPresentItemStack(): ItemStack | Returns the present ItemStack                    |
| getToBeWrappedStack(): ItemStack | Returns the item that will be wrapped            |
| setCancelled(boolean): Void      | Cancel the event                                 |



### PresentWrapEvent.Post

---

Available methods:

| Method                           | Purpose                                                |
| -------------------------------- | ------------------------------------------------------ |
| getPlayer(): Player              | Returns the player that is about to wrap an item       |
| getPresentItemStack(): ItemStack | Returns the present ItemStack                          |
| getPresentNBT(): PresentNBT      | Returns the NBT data that contains present information |

Code example:

```java
		@EventHandler
    public void onPresentWrap(PresentWrapEvent.Post e) {
        ItemStack is = e.getPresentItemStack();
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.RED + e.getPlayer().getName() + "'s Present!");
        is.setItemMeta(im);
    }
```

---



## Custom Presents

If you want to add your own custom ItemStack as presents, this is the place to be! <br>Any item can be a present, though by default Thieme's Presents only supports player heads as presents. Using this API you can write your own implementation to add custom presents.

To add a custom present, you will use the `Present` class. You can create a present as follows:

```jav
Present customPresent = new Present("iron_hoe_present", new ItemStack(Material.IRON_HOE));
```

Lets talk about the constructor. The constructor takes 2 parameters: a `String` and an `ItemStack`. <br>

The `String` has to be a unique string, that will represent the `NamespacedKey`, but this is also the string **Essentials** will use to give someone this item. 

The second argument `ItemStack` is just exactly what you expected it to do; this is the present item. So in this case you could use iron hoes as presents <img class="emote" src="https://cdn.discordapp.com/emojis/826356040597635084.png?size=32" alt=":stressed_smile:" title=":stressed_smile:">

BUT we're not there yet! We only created an `instance` of a Present. See [adding custom presents](#adding-custom-presents) on how to actually add it to the plugin.

Because first, we'll talk about a few more properties.

## Present Properties

Now you know how to make a custom present, we can talk about the possibilities with custom presents. <br>

Presents store a lot more information than just the `NamespacedKey` and an `ItemStack`. The most important two other properties are:

`ShapedRecipe` and `PresentNBT`. 

`ShapedRecipe` contains the recipe that can be used to craft the present

`PresentNBT` is a class of my own that holds all the data that's the inside nbt of a present.

Let's take a look at the public methods of the `Present` class:

| Method                                                      | Purpose                                                    |
| ----------------------------------------------------------- | ---------------------------------------------------------- |
| setRecipe(String[], HashMap<Character, MaterialData>): Void | Set the recipe for the custom present                      |
| removeRecipe(): Void                                        | Removes the recipe for the custom present                  |
| updatePresentNBT(): Void                                    | Set the PresentNBT property in the ItemMeta of the present |

**So what data does PresentNBT store?**

In version 1.0, presentNBT stores the following data:

```java
		// is signed
    public boolean isSigned = false;

    // present itself
    public String presentBase64 = null;

    // closed head url
    public String closed_head = null;

    // open head url
    public String open_head = null;
```

`isSigned` saves whether the present has been signed, as this is a more solid way than checking the lore. <br>`presentBase64` is an ItemStack encoded to base64, as later on this entire PresentNBT class gets encoded to one massive string. <br> `closed_head` is the long unique string for a minecraft texture. <br> `open_head` same as closed_head but different value.

Most likely this class will expand in the future, possibly saving information like `wrapped_date`, `wrapped_by` and `signed_by`.



## Adding Custom Presents

I won't keep you busy too much longer. It's quite simple to add a custom present to the plugin. <br>Let me just show you by an example:

```java
ThiemesPresents.getPresentManager().addPresent(customPresent); // Add present to the plugin
ThiemesPresents.getPresentManager().reloadEssentialItems(); // Add new presents to essentials db IMPORTANT!!
```

Voila, that's it. <br>You simply just call the main class, get the PresentManager and add a present.<br>Make sure you do call the `reloadEssentialItems` method if you're planning on using a /give command (highly recommended)



## Support

The API as of now is very limited, as the main priority is still put on the plugin itself. If you have any requests, please join the discord and request it there.

Invite link: https://discord.gg/U55dwsGPTn
