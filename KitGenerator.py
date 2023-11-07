
def inp(type):
    user = input(f"{type} (eg: diamond_{type}): ")
    if user:
        enchant = input("Enchantment (eg: protection 2): ").split(" ")
        code = ""
        code = code + f"\n    item = new ItemStack(Material.{user.upper()});"
        if enchant[0]:
            code = code + f"\n    item.addEnchantment(Enchantment.{enchant[0].upper()}, {enchant[1]}); // CHECK ENCHANT"
        code = code + f"\n    this.player.getInventory().set{type.title()}(item);"
        return code
    else:
        return ""

code = ""
code2 = ""
enchant = [""]
print("Please answer questions, or leave blank and press enter to choose nothing\nAll kits can be found here:\n    https://docs.google.com/spreadsheets/d/19AjEcBofWj3tTlZCbzQlgGsIxJ_DaLLrYeETvVY76Nc/edit?usp=sharing \n    (Please do level 1 kits for now)")
name = input("Kit name (eg: Swordsman): ")
code = code + f"private void {name}() {{\n    ItemStack item;\n    Potion pot;"

icon = input("Item for icon of kit: ")
code2 = code2 + f"item = new ItemStack(Material.{icon.upper()});\nitem.setAmount(1);\nmeta = item.getItemMeta();\nif (meta != null) {{\n    meta.setDisplayName(ChatColor.RED + \"{name}\");\n    item.setItemMeta(meta);\n}}\ninventory.setItem(2, item);"
# armour
code = code + inp("helmet")
code = code + inp("chestplate")
code = code + inp("leggings")
code = code + inp("boots")

item = input("Any other items\nFor potions, type 'potion' and answer questions: ")
while item != "":
    if item == "potion":
        ptype = input("Type of potion: ").upper()
        plevel = input("Potion level: ")
        amount = input("amount: ")
        code = code + f"\n    pot = new Potion(PotionType.{ptype.upper()}) // CHECK POTION;\n    pot.setLevel({plevel});\n    pot.setSplash(true);\n    item = pot.toItemStack({amount});\n    this.player.getInventory().addItem(item);"
    else:
        amount = input("amount: ")
        code = code + f"\n    item = new ItemStack(Material.{item.upper()}, {amount});"
        enchant = input("enchant: ").split(" ")
        if enchant[0]:
            code = code + f"\n    item.addEnchantment(Enchantment.{enchant[0].upper()}, {enchant[1]});" 
        enchant = [""] 
        code = code + f"\n    this.player.getInventory().addItem(item);"
    item = input("Other items: ")
buff = input("Describe buff shortly: ")
notes = input("Any final notes or questions: ")
code = code + f"\n    this.player.setMetadata(\"{name}\", new FixedMetadataValue(main.getPlugin(main.class), \"kit\"));\n}}"
code = code + f"\n// Buff: {buff}\n// {notes}"

print(f"\nHere is the code:\n<--------kitscards/Kits.java------------>\n")
print(code + f"\n\n<----------------------->")

print(f"\n<-----------lobby/inMenu.java----------->")
print(code2+"\n<---------------------->")
input(" ")