package example.naveen;

import example.naveen.entity.Address;
import example.naveen.entity.Customer;
import example.naveen.entity.Name;
import example.naveen.entity.Order;
import example.naveen.model.Location;
import example.naveen.model.OrderDto;
import example.naveen.model.User;
import example.naveen.repository.LocationRepository;
import example.naveen.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootDtoTutorialApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public SpringbootDtoTutorialApplication(UserRepository userRepository, LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDtoTutorialApplication.class, args);
    }


    @Override
    public void run(String... args) {

        Location location = new Location();
        location.setPlace("Pune");
        location.setDescription("Pune is great place to live");
        location.setLongitude(40.5);
        location.setLatitude(30.6);
        locationRepository.save(location);

        User user1 = new User();
        user1.setFirstName("Ramesh");
        user1.setLastName("Fadatare");
        user1.setEmail("ramesh@gmail.com");
        user1.setPassword("secret");
        user1.setLocation(location);
        userRepository.save(user1);

        User user2 = new User();
        user2.setFirstName("John");
        user2.setLastName("Cena");
        user2.setEmail("john@gmail.com");
        user2.setPassword("secret");
        user2.setLocation(location);
        userRepository.save(user2);

    }

    @Bean
    public CommandLineRunner demo(ModelMapper modelMapper) {
        return (args) -> {
            Order order = new Order();
            Address address = new Address();
            address.setCity("city");
            address.setStreet("street");
            order.setBillingAddress(address);
            Customer customer = new Customer();
            customer.setName(new Name("John", "Doe"));
            order.setCustomer(customer);
            System.out.println(mapToOrderDto(order, modelMapper));

        };
    }

    private static OrderDto mapToOrderDto(Order order, ModelMapper modelMapper) {
        modelMapper.typeMap(Order.class, OrderDto.class).addMappings(mapper -> {
            mapper.skip(OrderDto::setCustomerFirstName);//skipping mapping of customer first name
        });
        return modelMapper.map(order, OrderDto.class);
    }
}
