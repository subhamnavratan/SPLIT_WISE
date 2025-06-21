function signup() {
    const name = document.getElementById('name').value;
    const phone = document.getElementById('phone').value;

    fetch("http://localhost:8080/api/user/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ name, phone })
    })
    .then(response => {
        if (response.ok) {
            document.getElementById('signup-msg').innerText = "Signup successful. Redirecting to login...";
            setTimeout(() => {
                window.location.href = "login.html";
            }, 2000);
        } else {
            document.getElementById('signup-msg').innerText = "Signup failed. Try again.";
        }
    })
    .catch(error => console.error("Signup error:", error));
}
