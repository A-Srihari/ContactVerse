document.addEventListener("DOMContentLoaded", () => {
    let themeIcon = document.getElementById("themeToggleIcon");
    applyTheme(getTheme());

    if (themeIcon) {
        themeIcon.addEventListener("click", () => {
            let current = getTheme();
            let newTheme = current === "light" ? "dark" : "light";
            setTheme(newTheme);
            applyTheme(newTheme);
        });
    }
});

function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}



function applyTheme(theme) {
    const body = document.body;
    const navbar = document.querySelector(".navbar");

    if (theme === "dark") {
        body.classList.add("dark-mode");
        body.classList.remove("light-mode");

        if (navbar) {
            navbar.classList.remove("navbar-light-theme");
            navbar.classList.add("navbar-dark-theme");
        }
    } else {
        body.classList.add("light-mode");
        body.classList.remove("dark-mode");

        if (navbar) {
            navbar.classList.remove("navbar-dark-theme");
            navbar.classList.add("navbar-light-theme");
        }
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const closeAlertBtn = document.getElementById("close-alert");

    if (closeAlertBtn) {
        closeAlertBtn.addEventListener("click", function () {
            const token = document.querySelector('meta[name="_csrf"]').getAttribute("content");
            const header = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

            fetch("/invalidateMessage", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    [header]: token   // Add CSRF token here
                }
            })
                .then(response => response.text())
                .then(data => {
                    console.log(data); // Optional: log confirmation
                })
                .catch(error => {
                    console.error("Error clearing session message:", error);
                });
        });
    }
});


//? js for sidebar open/close
document.addEventListener("DOMContentLoaded", () => {
    const sidebar = document.querySelector(".sidebar");
    const mainContent = document.getElementById("main-content");
    const toggleBtn = document.getElementById("sidebarToggle");

    if (toggleBtn) {
        toggleBtn.addEventListener("click", () => {
            document.body.classList.toggle("sidebar-collapsed");
            sidebar.classList.toggle("collapsed");
        });
    }
});



//* JS for contact view, delete, edit
console.log("This is the script.js file")
const viewContactModal = document.getElementById('contact-modal-id');

const options = {
    backdrop: 'static',
    backdropClasses: 'position-fixed top-0 start-0 w-100 h-100 bg-dark bg-opacity-50',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

const instanceOptions = {
    id: 'contact-modal-id',
    overlay: true,
    override: true,
};


const contactModal = new bootstrap.Modal(viewContactModal, options);
// function onContactModalShow() {
//     contactModal.show(viewContactModal);
// }


// ! Updating the contact+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=
function openContactModal(contact) {
    console.log("Open contact modal with contact:", contact);
    // fill modal fields
    document.getElementById("contactName").textContent = contact.name;
    document.getElementById("contactEmail").textContent = contact.email;
    document.getElementById("contactPhone").textContent = contact.phoneNumber;
    document.getElementById("contactAddress").textContent = contact.address;
    document.getElementById("contactAbout").textContent = contact.about;
    document.getElementById("contactFavorite").textContent = contact.favorite ? "Yes" : "No";
    document.getElementById("contactInstagram").textContent = contact.instagram;
    document.getElementById("contactLinkedin").textContent = contact.linkedin;
    document.getElementById("contactSocial").textContent = contact.instagram;
    document.getElementById("contactProfile").src = contact.profilePic;

    // set the contact id in the edit button
    document.querySelector(".edit-contact-btn").setAttribute("data-id", contact);

    // open modal
    const modal = new bootstrap.Modal(document.getElementById("myCenteredScrollableModal"));
    modal.show();
}


document.addEventListener("DOMContentLoaded", () => {
    document.querySelector(".edit-contact-btn").addEventListener("click", function () {
        const contactId = this.getAttribute("data-id");
        console.log("Edit contact with ID:-->", contactId);
        if (contactId) {
            window.location.href = "/user/contact/edit/" + contactId;
        }
    });
});


//! Deleting the contact+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=
function deleteContact(id) {
    console.log("Delete contact with ID:", id);
    if (confirm("⚠️ Are you sure you want to delete this contact? This action cannot be undone.")) {
        const token = document.querySelector('meta[name="_csrf"]').getAttribute("content");
        const header = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

        fetch(`/user/contact/delete/${id}`, {
            method: "DELETE",
            headers: {
                [header]: token,   // ✅ CSRF protection header
                "Content-Type": "application/json"
            }
        })
            .then(res => {
                if (!res.ok) {
                    throw new Error("Failed to delete contact");
                }
                return res.text();
            })
            .then(msg => {
                alert(msg);
                location.reload();
            })
            .catch(err => {
                console.error("Error deleting contact:", err);
                alert("❌ Error deleting contact. Please try again.");
            });
    }
}


//! Viewing the contact+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=
function viewContact(contactId) {
    fetch(`/user/contact/${contactId}`)
        .then(res => res.json())
        .then(contact => {
            console.log("View contact with ID:", contactId);
            console.log(contact);
            document.getElementById("contactName").innerText = contact.name || "N/A";
            document.getElementById("contactProfile").src =  contact.profilePic || "/images/user.jpg";
            document.getElementById("contactEmail").innerText = contact.email || "N/A";
            document.getElementById("contactPhone").innerText = contact.phoneNumber || "N/A";
            document.getElementById("contactAddress").innerText = contact.address || "N/A";
            document.getElementById("contactAbout").innerText = contact.about || "N/A";
            document.getElementById("contactFavorite").innerText = contact.favorite ? "Yes" : "No";
            document.getElementById("contactInstagram").innerText = contact.instagram || "N/A";
            document.getElementById("contactLinkedin").innerText = contact.linkedin || "N/A";
            document.getElementById("contactSocial").innerText = contact.instagram || "N/A";

            let link1 = document.getElementById("contactInstagram");
            let link2 = document.getElementById("contactLinkedin");
            let link3 = document.getElementById("contactSocial");

            document.getElementById("contactInstagram").href = link1.innerText;
            document.getElementById("contactLinkedin").href = link2.innerText;
            document.getElementById("contactSocial").href = link3.innerText;

            document.getElementById("contactInstagram").innerText = link1.innerText;
            document.getElementById("contactLinkedin").innerText = link2.innerText;
            document.getElementById("contactSocial").innerText = link3.innerText;

            // finally open the modal
            new bootstrap.Modal(document.getElementById('myCenteredScrollableModal')).show();
        })
        .catch(err => console.error("Error fetching contact:", err));
}



//* js for displaying the contact details in the modal
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".contact-action-btns").forEach(button => {
        button.addEventListener("click", function () {
            const contactId = this.getAttribute("data-id");
            console.log("Loading contact with ID:", contactId);
            fetch(`/user/contact/${contactId}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Contact not found");
                    }
                    return response.json();
                })
                .then(contact => {
                    // Profile Pic
                    document.getElementById("contactProfile").src = contact.profilePic || "/images/logo.jpg";

                    // Basic details
                    document.getElementById("contactName").innerText = contact.name || "N/A";
                    document.getElementById("contactEmail").innerText = contact.email || "N/A";
                    document.getElementById("contactPhone").innerText = contact.phoneNumber || "N/A";
                    document.getElementById("contactAddress").innerText = contact.address || "N/A";
                    document.getElementById("contactAddress").innerText = contact.about || "N/A";
                    // Favorite
                    document.getElementById("contactFavorite").innerText = contact.favorite ? "⭐ Yes" : "No";

                    // Instagram
                    const instaEl = document.getElementById("contactInstagram");
                    if (contact.instagram) {
                        instaEl.innerText = contact.instagram;
                        instaEl.href = contact.instagram.startsWith("http") ? contact.instagram : "https://" + contact.instagram;
                    } else {
                        instaEl.innerText = "N/A";
                        instaEl.removeAttribute("href");
                    }

                    // LinkedIn
                    const linkedinEl = document.getElementById("contactLinkedin");
                    if (contact.linkedin) {
                        linkedinEl.innerText = contact.linkedin;
                        linkedinEl.href = contact.linkedin.startsWith("http") ? contact.linkedin : "https://" + contact.linkedin;
                    } else {
                        linkedinEl.innerText = "N/A";
                        linkedinEl.removeAttribute("href");
                    }

                    // Social Links
                    let socialsHtml = "";
                    if (contact.socialLinks && contact.socialLinks.length > 0) {
                        contact.socialLinks.forEach(linkObj => {
                            socialsHtml += `<a href="${linkObj.url}" target="_blank">${linkObj.platform || linkObj.url}</a><br>`;
                        });
                    } else {
                        socialsHtml = "No social links";
                    }
                    document.getElementById("contactSocial").innerHTML = socialsHtml;
                })
                .catch(error => {
                    console.error("Error fetching contact:", error);
                    document.getElementById("contactName").innerText = "Error loading contact";
                });
        });
    });
});


