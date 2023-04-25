from collections import defaultdict
import json

def load_categories_suppliers():
    # Load the Categories.json file into a dictionary
    with open('Categories.json') as f:
        Categories = json.load(f)

    # Load the Suppliers.json file into a dictionary
    with open('Suppliers.json', encoding="utf8") as f:
        Suppliers = json.load(f)

    # Create a dictionary to map category IDs to category names
    category_map = {}
    for category in Categories['activities']:
        category_map[category['id']] = category['name']

    # Replace category IDs with category names in the Suppliers.json file
    for supplier in Suppliers:
        category_names = []
        for category in supplier['categories']:
            if category in category_map:
                category_names.append(category_map[category])
            else:
                print("Category ID " + str(category) + " doesn't exists in Categories.json")
        supplier['categories'] = category_names    

    # Save the modified Suppliers.json file
    with open('Suppliers1.json', 'w') as f:
        json.dump(Suppliers, f, indent=4) 

def load_categories_activities():
    # Load the Categories.json file into a dictionary
    with open('Categories.json') as f:
        Categories = json.load(f)

    # Load the Activities.json file into a dictionary
    with open('Activities.json', encoding="utf8") as f:
        Activities = json.load(f)

    # Create a dictionary to map category IDs to category names
    category_map = {}
    for category in Categories['activities']:
        category_map[category['id']] = category['name']

    # Replace category IDs with category names in the Activities.json file
    for activity in Activities:
        category_names = []
        for category in activity['categoryIds']:
            if category in category_map:
                category_names.append(category_map[category])
            else:
                print("Category ID " + str(category) + " doesn't exists in Categories.json")
        activity['categories'] = category_names    

    # Save the modified Activities.json file
    with open('Activities1.json', 'w') as f:
        json.dump(Activities, f, indent=4) 

def add_suppliers_activities():
    # Load the Activities1.json file into a dictionary
    with open('Activities1.json', encoding="utf8") as f:
        Activities = json.load(f)

    # Load the Suppliers1.json file into a dictionary
    with open('Suppliers1.json', encoding="utf8") as f:
        Suppliers = json.load(f)
 
    groups = defaultdict(list)

    for obj in Activities:
        groups[obj['supplierId']].append(obj)

    company_list = []
    for supplier in Suppliers:
        activity_list = groups.get(supplier['id'])

        # Remove unwanted properties (supplierId, supplierName, supplierRsvpPhone) 
        # Keep  (id, name, island, times, startTimeMinutes, categoryIds , supplierOnlineReservations, imageUrls, description, notes, directions, transportationMandatory)
        for activity in activity_list:
            activity['activity'] = activity['name']
            activity['categoryIds'] = activity['categories']
            del activity['supplierId']
            del activity['supplierRsvpPhone']
            del activity['supplierName']
            del activity['name']

        supplier['activities'] = activity_list

    # Save the final output in Companies.json file
    with open('Companies.json', 'w') as f:
        json.dump(Suppliers, f, indent=4) 

  

load_categories_suppliers()
load_categories_activities()
add_suppliers_activities()

