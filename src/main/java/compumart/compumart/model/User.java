() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();
        if (getId() != null) {
            doc.append("_id", getId());
        }
        doc.append("fName", fName)
                .append("lName", lName)
                .append("email", email)
                .append("password", password)
                .append("phone", phone)
                .append("address", address)
                .append("role", role)
                .append("createdAt", createdAt);
        return doc;
    }

    public static User fromDocument(Document doc) {
        User user = new User();
        if (doc.containsKey("_id")) user.setId(doc.getObjectId("_id"));
        user.setfName(doc.getString("fName"));
        user.setlName(doc.getString("lName"));
        user.setEmail(doc.getString("email"));
        user.setPassword(doc.getString("password"));
        user.setPhone(doc.getString("phone"));
        user.setAddress(doc.getString("address"));
        user.setRole(doc.getString("role"));
        user.setCreatedAt(doc.getDate("createdAt"));
        return user;
    }
}
