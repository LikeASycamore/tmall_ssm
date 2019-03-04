package comparator;
 
import java.util.Comparator;
 
import com.how2java.tmall.pojo.Product;
 
/**
 * 销量比较器 把销量高的放前面
 * @author chenzhuo
 * date:2018年9月15日
 *
 */
public class ProductSaleCountComparator implements Comparator<Product>{
 
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getSaleCount()-p1.getSaleCount();
    }
 
}