public class Funcionario {
    private int id;
    private String nome;
    private String usuario;
    private String senha;
    private Cargo cargo;

    public Funcionario(int id, String nome, String usuario, String senha, Cargo cargo) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.cargo = cargo;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getUsuario() { return usuario; }
    public String getSenha() { return senha; }
    public Cargo getCargo() { return cargo; }
}