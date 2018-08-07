package cloud.jetstream.repository;

import cloud.jetstream.domain.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repository class for Quote objects to handle MySQL database queries and CRUD operations
 */
@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {

    @Query("SELECT q FROM Quote q WHERE q.symbol = :sym AND q.price = " +
            "(SELECT MAX(maxq.price) FROM Quote maxq " +
            "WHERE maxq.symbol = :sym AND maxq.date BETWEEN :date AND :endOfDate)")
    List<Quote> findDailyMax(@Param("sym") String sym, @Param("date") Date date, @Param("endOfDate") Date endOfDate);

    @Query("SELECT q FROM Quote q WHERE q.symbol = :sym AND q.price = " +
            "(SELECT MIN(minq.price) FROM Quote minq " +
            "WHERE minq.symbol = :sym AND minq.date BETWEEN :date AND :endOfDate)")
    List<Quote> findDailyMin(@Param("sym") String sym, @Param("date") Date date, @Param("endOfDate") Date endOfDate);

    @Query("SELECT SUM(q.volume) FROM Quote q " +
            "WHERE q.symbol = :sym AND q.date BETWEEN :date AND :endOfDate")
     int findDailyVolume(@Param("sym") String sym, @Param("date") Date date, @Param("endOfDate") Date endOfDate);

    @Query("SELECT q FROM Quote q WHERE q.symbol = :sym AND q.date = " +
            "(SELECT MAX(closeq.date) FROM Quote closeq " +
            "WHERE closeq.symbol = :sym AND closeq.date BETWEEN :date AND :endOfDate)")
    List<Quote> findClosingPrice(@Param("sym") String sym, @Param("date") Date date, @Param("endOfDate") Date endOfDate);

    @Query("SELECT q FROM Quote q WHERE q.symbol = :sym AND q.price =" +
            "(SELECT MAX(maxq.price) FROM Quote maxq " +
            "WHERE maxq.symbol = :sym AND maxq.date BETWEEN :monthStart AND :monthEnd)")
    List<Quote> findMonthlyMax(@Param("sym") String sym, @Param("monthStart") Date monthStart, @Param("monthEnd") Date monthEnd);

    @Query("SELECT q FROM Quote q WHERE q.symbol = :sym AND q.price =" +
            "(SELECT MIN(maxq.price) FROM Quote maxq " +
            "WHERE maxq.symbol = :sym AND maxq.date BETWEEN :monthStart AND :monthEnd)")
    List<Quote> findMonthlyMin(@Param("sym") String sym, @Param("monthStart") Date monthStart, @Param("monthEnd") Date monthEnd);

    @Query("SELECT SUM(q.volume) FROM Quote q " +
            "WHERE q.symbol = :sym AND q.date BETWEEN :monthStart AND :monthEnd")
    int findMonthlyVolume(@Param("sym") String sym, @Param("monthStart") Date monthStart, @Param("monthEnd") Date monthEnd);
}
