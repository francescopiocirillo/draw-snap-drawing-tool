package it.unisa.software_architecture_design.drawsnapdrawingtool.commands;

/**
 * Questa classe invoca il Comando che le è passato come parametro.
 * Serve a spezzare la dipendenza tra il client e i Commands.
 */
public class Invoker {

    /*
     * Attributi
     */
    Command command;

    /*
     * Costruttore, getter e setter
     */
    public Invoker() {    };

    public Invoker(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    /*
     * Logica della classe
     */

    public void executeCommand() {
        command.execute();
    }
}
