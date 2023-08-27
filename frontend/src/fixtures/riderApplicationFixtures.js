const riderApplicationFixtures = {
    oneRiderApplicationPending:
      {
        "id": 1,
        "status": "pending",
        "userId": 1,
        "perm_number": "1234567",
        "created_date": new Date("08/21/2023"),
        "updated_date": new Date("8/23/2023"),
        "description": "I sprained my ankle playing soccer.",
        "notes": "You should be getting approved soon."   
      },
    oneRiderApplicationCancelled:
      {
        "id": 2,
        "status": "cancelled",
        "userId": 1,
        "perm_number": "1234567",
        "created_date": new Date("08/21/2023"),
        "updated_date": new Date("8/23/2023"),
        "cancelled_date": new Date("8/26/2023"),
        "description": "I stubbed my toe.",
        "notes": "Walk it off, you'll be fine."   
      },
    threeRiderApplications: [
        {
            "id": 1,
            "status": "pending",
            "userId": 2,
            "perm_number": "1234567",
            "created_date": 8/21/2023,
            "updated_date": 8/23/2023,
            "cancelled_date": 8/26/2023,
            "description": "I sprained my ankle playing soccer.",
            "notes": "You should be getting approved soon"
            
        },
        {
            "id": 2,
            "status": "declined",
            "userId": 3,
            "perm_number": "5498712",
            "created_date": 8/17/2023,
            "updated_date": 8/19/2023,
            "cancelled_date": 8/21/2023,
            "description": "I dont wake up early enough to make it to class on time if I walk.",
            "notes": "This is not a valid reason. Please consider fixing your sleep schedule."
        },
        {
            "id": 3,
            "status": "accepted",
            "userId": 4,
            "perm_number": "6058944",
            "created_date": 8/19/2023,
            "updated_date": 8/21/2023,
            "cancelled_date": 8/23/2023,
            "description": "I broke both of my legs skateboarding.",
            "notes": "Get Better Soon!"
        },
    ]
}

export default riderApplicationFixtures;