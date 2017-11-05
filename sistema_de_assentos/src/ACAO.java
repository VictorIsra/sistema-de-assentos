
/*
** ENUMERA AS POSSÍVEIS AÇÕES DE UMA THREAD CLIENTE **
*/
public enum ACAO {
	RESERVAR_ASSENTO_ALEATORIO(0), RESERVAR_ASSENTO_livre(1), VER_MAPA_ASSENTOS(2);
	
	private final int valor;
    ACAO(int valorOpcao){
        valor = valorOpcao;
    }
    public int getValor(){
        return valor;
    }
}
