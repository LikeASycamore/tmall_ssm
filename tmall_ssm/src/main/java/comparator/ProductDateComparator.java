package comparator;
 
import java.util.Comparator;
 
import com.how2java.tmall.pojo.Product;
 
/**
 * 新品比较器 把创建日期晚的放前面
 * @author chenzhuo
 * date:2018年9月15日
 *
 */
public class ProductDateComparator implements Comparator<Product>{
 
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getCreateDate().compareTo(p1.getCreateDate());
    }
 
}