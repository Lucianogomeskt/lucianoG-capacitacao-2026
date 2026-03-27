package br.com.indra.jp_capacitacao_2026.service;

import br.com.indra.jp_capacitacao_2026.enums.StatusCarrinho;
import br.com.indra.jp_capacitacao_2026.model.Carrinho;
import br.com.indra.jp_capacitacao_2026.repository.CarrinhoRepository;
import br.com.indra.jp_capacitacao_2026.service.dto.CarrinhoRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.CarrinhoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;

    @Transactional
    public CarrinhoResponseDTO buscarOuCriarCarrinho(CarrinhoRequestDTO dto) {
        Carrinho carrinho = pegarOuCriar(dto);
        return converterParaDTO(carrinho);
    }

    private Carrinho pegarOuCriar(CarrinhoRequestDTO dto) {
        return carrinhoRepository.findByUsuarioIdAndStatus(dto.usuarioId(), StatusCarrinho.ATIVO)
                .orElseGet(() -> {

                    Carrinho novo = converterParaEntidade(dto);
                    return carrinhoRepository.save(novo);
                });
    }

    @Transactional(readOnly = true)
    public CarrinhoResponseDTO verCarrinho(Long usuarioId) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioIdAndStatus(usuarioId, StatusCarrinho.ATIVO)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado para o usuário: " + usuarioId));

        return converterParaDTO(carrinho);
    }

    private Carrinho converterParaEntidade(CarrinhoRequestDTO dto) {
        Carrinho carrinho = new Carrinho();
        carrinho.setUsuarioId(dto.usuarioId());
        carrinho.setStatus(StatusCarrinho.ATIVO);
        return carrinho;
    }

    private CarrinhoResponseDTO converterParaDTO(Carrinho carrinho) {
        BigDecimal total = BigDecimal.ZERO;

        return new CarrinhoResponseDTO(
                carrinho.getId(),
                carrinho.getUsuarioId(),
                carrinho.getStatus(),
                total
        );
    }
}