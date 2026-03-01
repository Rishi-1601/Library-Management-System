package com.library.repository;

import com.library.model.BorrowRecord;
import com.library.model.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    
    // Find by user ID
    List<BorrowRecord> findByUserId(Long userId);
    
    // Find by book ID
    List<BorrowRecord> findByBookId(Long bookId);
    
    // Find by user ID and status
    List<BorrowRecord> findByUserIdAndStatus(Long userId, BorrowStatus status);
    
    // Find by user ID, book ID and status
    Optional<BorrowRecord> findByUserIdAndBookIdAndStatus(Long userId, Long bookId, BorrowStatus status);
    
    // Find by status and due date before
    List<BorrowRecord> findByStatusAndDueDateBefore(BorrowStatus status, Date date);
    
    // Count by user ID and status
    @Query("SELECT COUNT(br) FROM BorrowRecord br WHERE br.user.id = :userId AND br.status = :status")
    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BorrowStatus status);
    
    // Find all overdue
    @Query("SELECT br FROM BorrowRecord br WHERE br.dueDate < :currentDate AND br.status = 'BORROWED'")
    List<BorrowRecord> findAllOverdue(@Param("currentDate") Date currentDate);
    
    // Find by status
    List<BorrowRecord> findByStatus(BorrowStatus status);
}