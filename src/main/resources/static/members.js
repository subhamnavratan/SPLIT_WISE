document.addEventListener("DOMContentLoaded", () => {
    const groupName = localStorage.getItem("currentGroup");
    const membersTableBody = document.querySelector("#membersTable tbody");
    const totalDisplay = document.getElementById("totalDisplay");
    const averageDisplay = document.getElementById("averageDisplay");
    const deleteGroupBtn = document.getElementById("deleteGroupBtn");

    // Load all members of the group
    function loadMembers() {
        fetch(`http://localhost:8080/members-db/${groupName}`)
            .then(res => res.json())
            .then(members => {
                membersTableBody.innerHTML = "";
                members.forEach(member => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                        <td>${member.phone}</td>
                        <td>${member.name}</td>
                        <td>₹${member.netAmount}</td>
                        <td>
                            <button onclick="deleteMember(${member.phone})">Delete</button>
                            <button onclick="viewDetails(${member.phone})">View Details</button>
                            <button onclick="addPayment(${member.phone})">Add Payment</button>
                        </td>
                    `;
                    membersTableBody.appendChild(row);
                });
                updateSummary();
            });
    }

    // Update total and average
    function updateSummary() {
        fetch(`http://localhost:8080/calculate/total/${groupName}`)
            .then(res => res.json())
            .then(total => totalDisplay.textContent = `Total: ₹${total}`);

        fetch(`http://localhost:8080/calculate/average/${groupName}`)
            .then(res => res.json())
            .then(avg => averageDisplay.textContent = `Average: ₹${avg.toFixed(2)}`);
    }

    // Add member
    window.addMember = async function () {
        const phone = document.getElementById("addPhone").value.trim();
        const name = document.getElementById("addName").value.trim();
        const amount = parseInt(document.getElementById("addAmount").value.trim()) || 0;

        if (!phone || !name) {
            alert("Phone and Name are required");
            return;
        }

        if (!/^\d{10}$/.test(phone)) {
            alert("Phone number must be exactly 10 digits.");
            return;
        }

        const newMember = {
            phone: parseInt(phone),
            name: name,
            netAmount: amount,
            detail: []
        };

        try {
            const response = await fetch(`http://localhost:8080/members-db/${groupName}/add`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(newMember)
            });

            if (!response.ok) throw new Error("Failed to add member");

            // ✅ Also add to GROUP collection
            await fetch(`http://localhost:8080/groups/add-member/${groupName}/${phone}`, {
                method: "PUT"
            });

            alert("Member added!");
            loadMembers();
        } catch (err) {
            console.error(err);
            alert("Error: " + err.message);
        }
    };

    // Delete member
    window.deleteMember = function (phone) {
        fetch(`http://localhost:8080/members-db/${groupName}/delete/${phone}`, { method: "DELETE" })
            .then(() => loadMembers())
            .catch(err => alert("Error deleting member: " + err.message));
    };

    // View payment history
    window.viewDetails = function (phone) {
        fetch(`http://localhost:8080/members-db/${groupName}`)
            .then(res => res.json())
            .then(members => {
                const member = members.find(m => m.phone === phone);
                if (!member || !member.detail || member.detail.length === 0) {
                    alert("No transactions found.");
                    return;
                }

                const history = member.detail.map(
                    d => `₹${d.amount} - ${d.description}`
                ).join("\n");

                alert(`Transaction history for ${member.name}:\n\n${history}`);
            });
    };

    // Add new payment
    window.addPayment = function (phone) {
        const amount = prompt("Enter payment amount:");
        const description = prompt("Enter description:");

        if (!amount || !description) {
            alert("Both fields are required.");
            return;
        }

        const payload = {
            amount: parseInt(amount),
            description: description
        };

        fetch(`http://localhost:8080/members-db/payment/${groupName}/${phone}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        })
            .then(res => {
                if (!res.ok) throw new Error("Failed to add payment.");
                return res.text();
            })
            .then(() => loadMembers())
            .catch(err => alert(err.message));
    };

    // Delete entire group
    deleteGroupBtn.addEventListener("click", () => {
        if (confirm("Are you sure you want to delete this group and all its members?")) {
            fetch(`http://localhost:8080/groups/delete/${groupName}`, { method: "DELETE" })
                .then(res => {
                    if (!res.ok) throw new Error("Group delete failed.");
                    alert("Group deleted successfully.");
                    window.location.href = "group.html";
                })
                .catch(err => alert("Error: " + err.message));
        }
    });

    // Initial load
    loadMembers();
});



