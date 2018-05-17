package me.rubick.hufu.logistics.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import me.rubick.hufu.logistics.app.model.Address;
import me.rubick.hufu.logistics.app.model.User;
import me.rubick.hufu.logistics.app.repository.AddressRepository;
import me.rubick.hufu.logistics.app.repository.UserRepository;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class UserService {
    @Resource
    private UserRepository userRepository;

    @Resource
    private AddressRepository addressRepository;

    public User findByCustomer(String customer) {
        return userRepository.findByCustomer(customer);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public Page<User> findByCustomerLike(Pageable pageable, String customer) {
        return userRepository.findByCustomerLike(customer, pageable);
    }

    public List<Address> getUnCheckAddress() {
        return addressRepository.findByVerifyNot("Ok");
    }

    public Address findAddress(Integer id) {
        return addressRepository.findOne(id);
    }

    public void saveAddress(Address address) {
        addressRepository.save(address);
    }

    public User findUser(Integer id) {
        return userRepository.findOne(id);
    }

    public void destroy(User user) {
        userRepository.delete(user);
    }

    public void destroyAddress(List<Address> addresses) {
        addressRepository.delete(addresses);
    }
}
