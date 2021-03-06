package com.community.dev.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.community.dev.persistence.Article;
import com.community.dev.persistence.Tag;
import com.community.dev.service.TagService;
import com.community.dev.util.PageWrapper;

@Controller
@RequestMapping("/tags")
public class TagController {

	private static final Logger logger = LoggerFactory.getLogger(TagController.class);

	@Autowired
	private TagService tagService;

	@GetMapping
	public String list(Model model,
			@PageableDefault(sort = { "tagId" }, direction = Direction.DESC, size = 20) Pageable pageable) {

		PageWrapper<Tag> page = new PageWrapper<Tag>(tagService.findAllByOrderByTagName(pageable), "/tags");
		model.addAttribute("page", page);

		return "tags/list";
	}

	@GetMapping("/createForm")
	public String createForm(Article article) {
		return "tags/createForm";
	}

	@PostMapping
	public String create(Tag tag) {
		tagService.save(tag);

		return "redirect:/tags";
	}

	@PutMapping
	public String update(Tag tag) {
		Tag oldTag = tagService.findByTagId(tag.getTagId());

		if (oldTag == null) {
			logger.info("tagId=[{}] not found.", tag.getTagId());
			return "redirect:/tags";
		}

		oldTag.setTagName(tag.getTagName());

		tagService.save(oldTag);
		return "redirect:/tags";
	}

	@GetMapping("/{tagId}/updateForm")
	public String updateForm(@PathVariable Long tagId, Model model) {
		model.addAttribute("tag", tagService.findByTagId(tagId));
		return "tags/updateForm";
	}

	@GetMapping("/{tagId}")
	public String form(@PathVariable Long tagId, Model model) {

		Tag tag = tagService.findByTagId(tagId);
		model.addAttribute("tag", tag);

		return "tags/form";
	}

}
