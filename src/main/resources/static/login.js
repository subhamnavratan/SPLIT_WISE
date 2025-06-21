// login.js

document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("login-form").addEventListener("submit", function (e) {
        e.preventDefault(); // Prevent form reload

        const phone = document.getElementById("phone").value.trim();
        const msg = document.getElementById("login-msg");

        if (!phone || isNaN(phone) || phone.length < 8) {
            msg.textContent = "Please enter a valid phone number.";
            msg.style.color = "red";
            return;
        }

        fetch(`http://localhost:8080/api/user/login/${phone}`)
            .then(response => {
                if (response.ok) {
                    // Store phone in localStorage
                    localStorage.setItem("loggedInPhone", phone);

                    // Redirect to group page (was dashboard.html before)
                    window.location.href = "group.html";
                } else {
                    msg.textContent = "Phone number not found. Please sign up first.";
                    msg.style.color = "red";
                }
            })
            .catch(error => {
                console.error("Login failed:", error);
                msg.textContent = "Error connecting to server.";
                msg.style.color = "red";
            });
    });
});
