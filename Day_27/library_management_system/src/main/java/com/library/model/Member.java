package Day_27.library_management_system.src.main.java.com.library.model;

import java.time.LocalDate;

public class Member {
    private int memberId;
    private String name;
    private String email;
    private String phone;
    private LocalDate joinDate;
    private MembershipType membershipType;

    // Enum for membership types
    public enum MembershipType {
        BASIC, PREMIUM
    }

    // Constructors
    public Member() {}

    public Member(String name, String email, String phone, MembershipType membershipType) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipType = membershipType;
        this.joinDate = LocalDate.now();
    }

    public Member(int memberId, String name, String email, String phone,
                  LocalDate joinDate, MembershipType membershipType) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.joinDate = joinDate;
        this.membershipType = membershipType;
    }

    // Getters and Setters
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    public MembershipType getMembershipType() { return membershipType; }
    public void setMembershipType(MembershipType membershipType) { this.membershipType = membershipType; }

    // Business methods
    public int getMaxBooksAllowed() {
        return membershipType == MembershipType.PREMIUM ? 10 : 3;
    }

    public int getLoanDurationDays() {
        return membershipType == MembershipType.PREMIUM ? 21 : 14;
    }

    @Override
    public String toString() {
        return String.format("Member{id=%d, name='%s', email='%s', type=%s, joinDate=%s}",
                memberId, name, email, membershipType, joinDate);
    }
}