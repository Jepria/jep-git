package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.entrance;
 
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.${moduleName}MainClientFactoryImpl;
import com.technology.jep.jepria.client.entrance.JepEntryPoint;

public class ${moduleName}EntryPoint extends JepEntryPoint {
    
  ${moduleName}EntryPoint() {
    super(${moduleName}MainClientFactoryImpl.getInstance());
  }
}
