package Day_27.library_management_system.src.main.java.com.library.dao;

import Day_27.library_management_system.src.main.java.com.library.model.Member;
import Day_27.library_management_system.src.main.java.com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    private static final String INSERT_MEMBER =
            "INSERT INTO members (name, email, phone, join_date, membership_type) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_MEMBER_BY_ID =
            "SELECT * FROM members WHERE member_id = ?";
    private static final String SELECT_ALL_MEMBERS =
            "SELECT * FROM members ORDER BY name";
    private static final String UPDATE_MEMBER =
            "UPDATE members SET name = ?, email = ?, phone = ?, membership_type = ? WHERE member_id = ?";
    private static final String DELETE_MEMBER =
            "DELETE FROM members WHERE member_id = ?";
    private static final String CHECK_EMAIL_EXISTS =
            "SELECT COUNT(*) FROM members WHERE email = ? AND member_id != ?";
    private static final String GET_MEMBER_LOAN_COUNT =
            "SELECT COUNT(*) FROM book_loans WHERE member_id = ? AND status = 'ACTIVE'";
    private static final String SEARCH_MEMBERS_BY_NAME =
            "SELECT * FROM members WHERE name LIKE ? ORDER BY name";

    public boolean createMember(Member member) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Check if email already exists
            if (emailExists(member.getEmail(), 0)) {
                System.out.println("Member with email " + member.getEmail() + " already exists!");
                return false;
            }

            pstmt = conn.prepareStatement(INSERT_MEMBER, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPhone());
            pstmt.setDate(4, Date.valueOf(member.getJoinDate()));
            pstmt.setString(5, member.getMembershipType().toString());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    member.setMemberId(generatedKeys.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error creating member: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    public Member getMemberById(int memberId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_MEMBER_BY_ID);
            pstmt.setInt(1, memberId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractMemberFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving member: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return null;
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_ALL_MEMBERS);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving members: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return members;
    }

    public boolean updateMember(Member member) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Check if email already exists for different member
            if (emailExists(member.getEmail(), member.getMemberId())) {
                System.out.println("Email " + member.getEmail() + " is already in use!");
                return false;
            }

            pstmt = conn.prepareStatement(UPDATE_MEMBER);
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getMembershipType().toString());
            pstmt.setInt(5, member.getMemberId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    public boolean deleteMember(int memberId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Check if member has active loans
            int activeLoanCount = getActiveLoanCount(memberId);
            if (activeLoanCount > 0) {
                System.out.println("Cannot delete member - has " + activeLoanCount + " active loans");
                return false;
            }

            pstmt = conn.prepareStatement(DELETE_MEMBER);
            pstmt.setInt(1, memberId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting member: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    public List<Member> searchMembersByName(String namePattern) {
        List<Member> members = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SEARCH_MEMBERS_BY_NAME);
            pstmt.setString(1, "%"+namePattern+"%");
            rs = pstmt.executeQuery();

            while(rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching members: "+e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return members;
    }

    public int getActiveLoanCount(int memberId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(GET_MEMBER_LOAN_COUNT);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting loan count: "+e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return 0;
    }

    private boolean emailExists(String email, int excludeMemberId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(CHECK_EMAIL_EXISTS);
            pstmt.setString(1,email);
            pstmt.setInt(2, excludeMemberId);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence: "+ e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return false;
    }

    private Member extractMemberFromResultSet(ResultSet rs) throws SQLException {
        return new Member(
                rs.getInt("member_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getDate("join_date").toLocalDate(),
                Member.MembershipType.valueOf(rs.getString("membership_type"))
        );
    }

    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
