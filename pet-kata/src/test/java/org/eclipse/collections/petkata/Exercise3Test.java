/*
 * Copyright (c) 2017 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.petkata;

import org.eclipse.collections.api.bag.Bag;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.Multimap;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * In the slides leading up to this exercise you should have learned about the following APIs.
 * <p/>
 * {@link Bag}<br>
 * {@link MutableBag}<br>
 * {@link MutableList#toBag()}<br>
 * <br>
 * {@link Multimap}<br>
 * {@link MutableList#groupBy(Function)}<br>
 * {@link MutableList#groupByEach(Function)}<br>
 * {@link Multimaps}<br>
 *
 * @see <a href="http://eclipse.github.io/eclipse-collections-kata/pet-kata/#/6">Exercise 3 Slides</a>
 */
public class Exercise3Test extends PetDomainForKata
{
    @Test
    public void getCountsByPetType()
    {
        MutableList<PetType> petTypes = this.people.flatCollect(Person::getPets).collect(Pet::getType);

        // Do you recognize this pattern?
        MutableMap<PetType, Integer> petTypeCounts = Maps.mutable.empty();
        for (PetType petType : petTypes)
        {
            Integer count = petTypeCounts.get(petType);
            if (count == null)
            {
                count = 0;
            }
            petTypeCounts.put(petType, count + 1);
        }

        Assert.assertEquals(Integer.valueOf(2), petTypeCounts.get(PetType.CAT));
        Assert.assertEquals(Integer.valueOf(2), petTypeCounts.get(PetType.DOG));
        Assert.assertEquals(Integer.valueOf(2), petTypeCounts.get(PetType.HAMSTER));
        Assert.assertEquals(Integer.valueOf(1), petTypeCounts.get(PetType.SNAKE));
        Assert.assertEquals(Integer.valueOf(1), petTypeCounts.get(PetType.TURTLE));
        Assert.assertEquals(Integer.valueOf(1), petTypeCounts.get(PetType.BIRD));

        // Hint: use the appropriate method on this.people to create a Bag<PetType>

        Bag<PetType> counts = this.people.flatCollect(p->p.getPetTypes()).toBag();
        
        Assert.assertEquals(2, counts.occurrencesOf(PetType.CAT));
        Assert.assertEquals(2, counts.occurrencesOf(PetType.DOG));
        Assert.assertEquals(2, counts.occurrencesOf(PetType.HAMSTER));
        Assert.assertEquals(1, counts.occurrencesOf(PetType.SNAKE));
        Assert.assertEquals(1, counts.occurrencesOf(PetType.TURTLE));
        Assert.assertEquals(1, counts.occurrencesOf(PetType.BIRD));
        
        MutableBag<PetType> countsMultiBag = this.people.flatCollect(Person::getPets).countBy(Pet::getType);
        Assert.assertEquals(2, countsMultiBag.occurrencesOf(PetType.CAT));
        Assert.assertEquals(2, countsMultiBag.occurrencesOf(PetType.DOG));
        Assert.assertEquals(2, countsMultiBag.occurrencesOf(PetType.HAMSTER));
        Assert.assertEquals(1, countsMultiBag.occurrencesOf(PetType.SNAKE));
        Assert.assertEquals(1, countsMultiBag.occurrencesOf(PetType.TURTLE));
        Assert.assertEquals(1, countsMultiBag.occurrencesOf(PetType.BIRD));
    }

    @Test
    public void getPeopleByLastName()
    {
        // Do you recognize this pattern?
        MutableMap<String, MutableList<Person>> lastNamesToPeople = Maps.mutable.empty();
        for (Person person : this.people)
        {
            String lastName = person.getLastName();
            MutableList<Person> peopleWithLastName = lastNamesToPeople.get(lastName);
            if (peopleWithLastName == null)
            {
                peopleWithLastName = Lists.mutable.empty();
                lastNamesToPeople.put(lastName, peopleWithLastName);
            }
            peopleWithLastName.add(person);
        }
        Verify.assertIterableSize(3, lastNamesToPeople.get("Smith"));

		// Hint: use the appropriate method on this.people to create a Multimap<String, Person>
        Multimap<String, Person> byLastNameMultimap = this.people.groupBy(Person::getLastName);
        //the same with functions!
        Function <Person,String> functionLastNamePerson = person -> person.getLastName();
        Multimap<String, Person> byLastNameMultimapUsingFunction =  this.people.groupBy(functionLastNamePerson);

        Verify.assertIterableSize(3, byLastNameMultimap.get("Smith"));
        Verify.assertIterableSize(3, byLastNameMultimapUsingFunction .get("Smith"));
    }

    @Test
    public void getPeopleByTheirPets()
    {
        // Do you recognize this pattern?
        MutableMap<PetType, MutableSet<Person>> peopleByPetType = Maps.mutable.empty();

        for (Person person : this.people)
        {
            MutableList<Pet> pets = person.getPets();
            for (Pet pet : pets)
            {
                PetType petType = pet.getType();
                MutableSet<Person> peopleWithPetType = peopleByPetType.get(petType);
                if (peopleWithPetType == null)
                {
                    peopleWithPetType = Sets.mutable.empty();
                    peopleByPetType.put(petType, peopleWithPetType);
                }
                peopleWithPetType.add(person);
            }
        }

        Verify.assertIterableSize(2, peopleByPetType.get(PetType.CAT));
        Verify.assertIterableSize(2, peopleByPetType.get(PetType.DOG));
        Verify.assertIterableSize(1, peopleByPetType.get(PetType.HAMSTER));
        Verify.assertIterableSize(1, peopleByPetType.get(PetType.TURTLE));
        Verify.assertIterableSize(1, peopleByPetType.get(PetType.BIRD));
        Verify.assertIterableSize(1, peopleByPetType.get(PetType.SNAKE));

        
		// Hint: use the appropriate method on this.people with a target collection to create a MutableSetMultimap<String, Person>
        // Hint: this.people is a MutableList, so it will return a MutableListMultimap without a target collection
        MutableSetMultimap<PetType, Person> peopleByPetType_MS = this.people.groupByEach(person -> person.getPetTypes(),Multimaps.mutable.set.empty());

        this.people.groupBy(person->person.getPets().collect(Pet::getType));
        Verify.assertIterableSize(2, peopleByPetType_MS .get(PetType.CAT));
        Verify.assertIterableSize(2, peopleByPetType_MS .get(PetType.DOG));
        Verify.assertIterableSize(1, peopleByPetType_MS .get(PetType.HAMSTER));
        Verify.assertIterableSize(1, peopleByPetType_MS .get(PetType.TURTLE));
        Verify.assertIterableSize(1, peopleByPetType_MS .get(PetType.BIRD));
        Verify.assertIterableSize(1, peopleByPetType_MS .get(PetType.SNAKE));
    }
}
