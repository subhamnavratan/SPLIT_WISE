document.addEventListener("DOMContentLoaded", function () {
    const phone = localStorage.getItem("loggedInPhone");
    const groupList = document.getElementById("group-list");
    const groupForm = document.getElementById("create-group-form");
    const groupMsg = document.getElementById("group-msg");

    // Load groups
    fetch(`http://localhost:8080/groups/phone/${phone}`)
        .then(res => res.json())
        .then(groups => {
            groupList.innerHTML = "";
            if (groups.length === 0) {
                groupList.innerHTML = "<p>No groups found. Create one below.</p>";
                return;
            }

            groups.forEach(group => {
                const div = document.createElement("div");
                div.className = "group-item";
                div.textContent = group.groupName;
                div.addEventListener("click", () => {
                    localStorage.setItem("currentGroup", group.groupName);
                    window.location.href = "members.html";
                });
                groupList.appendChild(div);
            });
        })
        .catch(err => {
            groupMsg.textContent = "Error loading groups.";
            groupMsg.style.color = "red";
            console.error(err);
        });

    // Create group
    groupForm.addEventListener("submit", function (e) {
        e.preventDefault();
        const groupName = document.getElementById("new-group-name").value.trim();
        if (!groupName) return;

        fetch("http://localhost:8080/groups", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                groupName: groupName,
                memberPhones: [parseInt(phone)]
            })
        })
        .then(res => {
            if (!res.ok) throw new Error("Failed to create group");
            return res.json();
        })
        .then(() => {
            location.reload(); // Reload to show new group
        })
        .catch(err => {
            groupMsg.textContent = "Failed to create group.";
            groupMsg.style.color = "red";
            console.error(err);
        });
    });
});


