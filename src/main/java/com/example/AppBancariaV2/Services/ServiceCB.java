package com.example.AppBancariaV2.Services;

import com.example.AppBancariaV2.AppBancariaV2Application;
import com.example.AppBancariaV2.ClienteRepository;
import com.example.AppBancariaV2.Entity.Cliente;
import com.example.AppBancariaV2.Entity.CuentaBancaria;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ServiceCB {
    ApplicationContext context = SpringApplication.run(AppBancariaV2Application.class);
    ClienteRepository repository = context.getBean(ClienteRepository.class);
    Scanner leer = new Scanner(System.in);
    List<Cliente> listaClientes = repository.findAll();
    public void menuCreador(){
        int var;
        boolean band = false;
        while(!band) {
            System.out.println("_________MENU_________");
            System.out.println("__Ingrese una opcion__");
            System.out.println("1: Soy cliente");
            System.out.println("2: No soy cliente");
            System.out.println("3: Salir");
            try {
                var = leer.nextInt();
                switch (var) {
                    case 1:
                        ingresar();
                        break;
                    case 2:
                        if (comprobarCreacion()) continue;
                        break;
                    case 3:
                        System.out.println("...SALIENDO...");
                        devuelveLista();
                        System.exit(1);
                    default:
                        System.out.println("Numero invalido");
                }
                band = true;
            }catch (InputMismatchException e){
                System.out.println("Debe ingresar un numero, error: " + e.getMessage());
                leer.next();
            }
        }
    }

    private void menuCliente(Cliente c){
        int var;
        boolean band = false;
        while(!band){
            System.out.println("_________MENU CLIENTE_________");
            System.out.println("__Ingrese una opcion__");
            System.out.println("1: Transferencia");
            System.out.println("2: Extraccion");
            System.out.println("3: Deposito");
            System.out.println("4: Consultar saldo");
            System.out.println("5: Actualizar informacion");
            System.out.println("6: Eliminar cuenta");
            System.out.println("7: Salir menu inicial");
            try {
                var = leer.nextInt();
                switch (var) {
                    case 1:
                        transferencia(c);
                        continue;
                    case 2:
                        extraer(c);
                        continue;
                    case 3:
                        deposito(c);
                        continue;
                    case 4:
                        System.out.println("Su saldo es de: " + c.getCuentaBancaria().getMonto());
                        continue;
                    case 5:
                        actualizarDatos(c);
                        continue;
                    case 6:
                        eliminarCuenta(c);
                    case 7:
                        System.out.println("...Saliendo al menu inicial...");
                        menuCreador();
                        continue;
                    default:
                        System.out.println("Opcion invalida");
                        break;
                }
                band = true;
            }catch (InputMismatchException e){
                System.out.println("Debe ingresar un numero, error: " + e.getMessage());
                leer.next();
            }
        }
    }
    //METODOS PARA CLIENTE
    private void crearCliente(){
        boolean band = false;
        while (!band) {
            try {
                Cliente cliente = new Cliente();
                System.out.println("Ingrese su nombre: ");
                cliente.setNombre(leer.next());
                System.out.println("Ingrese su apellido: ");
                cliente.setApellido(leer.next());
                System.out.println("Ingrese su edad: ");
                int edad = leer.nextInt();
                if (edad < 18) {
                    System.out.println("No eres mayor de edad, no puedes acceder a una cuenta bancaria");
                    System.exit(1);
                } else {
                    cliente.setEdad(edad);
                }
                cliente.setCuentaBancaria(crearCuenta());
                repository.save(cliente); //GUARDO EL CLIENTE EN MYSQL
                band = true;
            } catch (InputMismatchException | NullPointerException | NumberFormatException e) {
                System.out.println("Debe ingresar un numero, error: " + e.getMessage());
                leer.next();
            }
        }
    }

    private void ingresar() {
        System.out.println("Ingrese su usuario: ");
        String user = leer.next();
        System.out.println("Ingrese su contraseña: ");
        String pass = leer.next();
        boolean band = true;
        for (Cliente aux:listaClientes) {
            if (user.equals(aux.getCuentaBancaria().getUsuario()) && pass.equals(aux.getCuentaBancaria().getPass())){
                band = true;
                menuCliente(aux);
            }else{
                band = false;
            }
        }
        if (!band) {
            System.out.println("Contraseña y/o usuario incorrecto");
            ingresar();
        }
    }

    public void devuelveLista(){
        System.out.println(repository.findAll());
    }
    private boolean comprobarCreacion() {
        System.out.println("Quieres crearte una cuenta? si/no");
        String res = leer.next();
        if (res.equalsIgnoreCase("si")) {
            if (repository.count() < 10) {
                crearCliente();
            } else {
                System.out.println("Limite de clientes excedido");
            }
            return true;
        } else if (res.equalsIgnoreCase("no")) {
            System.out.println("..REGRESANDO AL MENU INICIAL..");
            menuCreador();
        }else{
            System.out.println("Debe colocar si o no");
            comprobarCreacion();
        }
        return false;
    }

    //METODOS PARA CUENTA BANCARIA
    private CuentaBancaria crearCuenta(){
        System.out.println("Cree un usuario (DEBE RECORDAR SU USUARIO): ");
        String user = leer.next();
        for (Cliente aux:listaClientes){
            if (user.equals(aux.getCuentaBancaria().getUsuario())){
                System.out.println("Ese usuario ya existe, ingrese otro");
                user = leer.next();
            }
        }
        System.out.println("Cree una contraseña (DEBE RECORDAR SU CONTRASEÑA): ");
        String contra = leer.next();
        if (contra.length() < 8){
            System.out.println("Necesita como minimo 8 caracteres");
            contra = leer.next();
        }
        return new CuentaBancaria(user, contra);
    }
    private void transferencia(Cliente c) {
        System.out.println("A cual usuario desea transferir?");
        String user = leer.next();
        for (Cliente aux:listaClientes){
            if (user.equals(aux.getCuentaBancaria().getUsuario())){
                System.out.println("Cual es el monto que desea transferir?");
                int montoTransferencia = leer.nextInt();
                if (montoTransferencia > c.getCuentaBancaria().getMonto()){
                    System.out.println("No posee ese monto, su monto actual es de: " + c.getCuentaBancaria().getMonto());
                }else{
                    int montoDebitar = c.getCuentaBancaria().getMonto() - montoTransferencia;
                    c.getCuentaBancaria().setMonto(montoDebitar);
                    actualizarMonto(c, montoDebitar);
                    int montoActual = aux.getCuentaBancaria().getMonto(); //Busco el monto actual de la cuenta a transferir
                    int mont = montoActual+montoTransferencia; //Sumo el monto actual mas el monto a transferir
                    aux.getCuentaBancaria().setMonto(mont); //Seteo el monto a la entidad
                    actualizarMonto(aux, mont); //Actualizo con mi update el monto en la base de datos
                }
            }
        }
    }
    private void deposito(Cliente c) {
        try {
            System.out.println("Monto a depositar?: ");
            int montoDeposito = leer.nextInt();
            int montoTotalDeposito = c.getCuentaBancaria().getMonto() + montoDeposito;
            c.getCuentaBancaria().setMonto(montoTotalDeposito);
            actualizarMonto(c, montoTotalDeposito); //UPDATE BASE DE DATOS
        }catch (InputMismatchException e){
            System.out.println("Debe ingresar un numero");
            deposito(c);
        }
    }
    private void extraer(Cliente c) {
        try {
            System.out.println("Monto a extraer: ");
            int montoDebito = leer.nextInt();
            if (montoDebito > c.getCuentaBancaria().getMonto()){
                System.out.println("No tiene ese monto, su monto actual es de: " + c.getCuentaBancaria().getMonto());
            }else{
                int montoTotal = c.getCuentaBancaria().getMonto() - montoDebito;
                c.getCuentaBancaria().setMonto(montoTotal);
                actualizarMonto(c, montoTotal); //UPDATE BASE DE DATOS
            }
        }catch (InputMismatchException e){
            System.out.println("Debe ingresar un numero");
            extraer(c);
        }
    }
    // METODOS Update Delete
    public void actualizarDatos(Cliente c){
        Long id = c.getId(); // busco el id del cliente a modificar
        System.out.println("Ingresa nombre actualizado");
        String name = leer.next();
        System.out.println("Ingresa apellido actualizado");
        String lastname = leer.next();
        System.out.println("Ingresa edad actualizada");
        int age = leer.nextInt();
        Cliente cli = new Cliente(id, name, lastname, age, c.getCuentaBancaria());
        repository.save(cli); //Actualizo con el save el cliente con el id
        devuelveLista();
    }

    public void eliminarCuenta(Cliente c){
        Long id = c.getId(); //Busco el id del cliente a eliminar
        repository.deleteById(id); //Elimino con el id
    }

    public void actualizarMonto(Cliente c, int pesitos){
        CuentaBancaria cuenta = c.getCuentaBancaria(); //Asigno a cuenta la cuenta de mi cliente
        cuenta.setMonto(pesitos); //Seteo a la entidad el monto
        Cliente cli = new Cliente(
                c.getId(),
                c.getNombre(),
                c.getApellido(),
                c.getEdad(),
                cuenta
        ); //Asigno a cli un nuevo cliente con todos los datos de mi cliente
        repository.save(cli); //Actualizo mi cliente con los nuevos datos
    }
}
