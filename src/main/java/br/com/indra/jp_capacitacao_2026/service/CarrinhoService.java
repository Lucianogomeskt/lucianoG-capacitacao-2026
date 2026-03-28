package br.com.indra.jp_capacitacao_2026.service;

import br.com.indra.jp_capacitacao_2026.enums.StatusCarrinho;
import br.com.indra.jp_capacitacao_2026.exception.CarrinhoNaoEncontrado;
import br.com.indra.jp_capacitacao_2026.exception.ProdutoNaoEncontradoException;
import br.com.indra.jp_capacitacao_2026.model.Carrinho;
import br.com.indra.jp_capacitacao_2026.repository.CarrinhoItemRepository;
import br.com.indra.jp_capacitacao_2026.repository.CarrinhoRepository;
import br.com.indra.jp_capacitacao_2026.service.dto.CarrinhoItemResponseDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.CarrinhoRequestDTO;
import br.com.indra.jp_capacitacao_2026.service.dto.CarrinhoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final CarrinhoItemRepository carrinhoItemRepository;

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
                .orElseThrow(() -> new CarrinhoNaoEncontrado("Carrinho não encontrado para o usuário: " + usuarioId));

        return converterParaDTO(carrinho);
    }

    @Transactional
    public void removerItem(Long carrinhoItemId) {
        if (!carrinhoItemRepository.existsById(carrinhoItemId)) {
            throw new ProdutoNaoEncontradoException("Item não encontrado no carrinho: " + carrinhoItemId);
        }
        carrinhoItemRepository.deleteById(carrinhoItemId);
    }

    @Transactional
    public void esvaziarCarrinho(Long usuarioId) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioIdAndStatus(usuarioId, StatusCarrinho.ATIVO)
                .orElseThrow(() -> new CarrinhoNaoEncontrado("Carrinho ativo não encontrado para esvaziar."));
        carrinho.getItens().clear();
        carrinhoRepository.save(carrinho);
    }

    private Carrinho converterParaEntidade(CarrinhoRequestDTO dto) {
        Carrinho carrinho = new Carrinho();
        carrinho.setUsuarioId(dto.usuarioId());
        carrinho.setStatus(StatusCarrinho.ATIVO);
        return carrinho;
    }

    private CarrinhoResponseDTO converterParaDTO(Carrinho carrinho) {
        List<CarrinhoItemResponseDTO> itensDTO = carrinho.getItens().stream()
                .map(item -> {
                    BigDecimal subtotal = item.getPriceSnapshot()
                            .multiply(BigDecimal.valueOf(item.getQuantidade()));

                    return new CarrinhoItemResponseDTO(
                            item.getId(),
                            item.getProduto().getId(),
                            item.getQuantidade(),
                            item.getPriceSnapshot(),
                            subtotal
                    );
                })
                .toList();

        BigDecimal totalGeral = itensDTO.stream()
                .map(CarrinhoItemResponseDTO::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CarrinhoResponseDTO(
                carrinho.getId(),
                carrinho.getUsuarioId(),
                carrinho.getStatus(),
                itensDTO,
                totalGeral
        );
    }
}